package com.hotel.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReservationServer {
    private static final Logger logger = Logger.getLogger(ReservationServer.class.getName());
    private Server server;
    private Connection dbConnection;

    private void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "9090"));
        server = ServerBuilder.forPort(port)
                .addService(new ReservationServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ReservationServer.this.stop();
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final ReservationServer server = new ReservationServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class ReservationServiceImpl extends ReservationServiceGrpc.ReservationServiceImplBase {
        private Connection getConnection() throws SQLException {
            String url = System.getenv().getOrDefault("DATABASE_URL", 
                "jdbc:postgresql://localhost:5432/hotel_reservation");
            String user = System.getenv().getOrDefault("DB_USER", "postgres");
            String password = System.getenv().getOrDefault("DB_PASSWORD", "postgres");
            return DriverManager.getConnection(url, user, password);
        }

        @Override
        public void getReservation(GetReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
            try (Connection conn = getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT r.*, c.id as c_id, c.nom, c.prenom, c.email, c.telephone, " +
                    "ch.id as ch_id, ch.type, ch.prix, ch.disponible " +
                    "FROM reservation r " +
                    "JOIN client c ON r.client_id = c.id " +
                    "JOIN chambre ch ON r.chambre_id = ch.id " +
                    "WHERE r.id = ?"
                );
                stmt.setLong(1, request.getId());
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    responseObserver.onNext(buildReservationResponse(rs));
                }
                responseObserver.onCompleted();
            } catch (SQLException e) {
                responseObserver.onError(e);
            }
        }

        @Override
        public void createReservation(CreateReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
            try (Connection conn = getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO reservation (client_id, chambre_id, date_debut, date_fin, preferences, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, NOW(), NOW()) RETURNING id"
                );
                stmt.setLong(1, request.getClientId());
                stmt.setLong(2, request.getChambreId());
                stmt.setString(3, request.getDateDebut());
                stmt.setString(4, request.getDateFin());
                stmt.setString(5, request.getPreferences());

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    GetReservationRequest getReq = GetReservationRequest.newBuilder().setId(id).build();
                    getReservation(getReq, responseObserver);
                }
            } catch (SQLException e) {
                responseObserver.onError(e);
            }
        }

        @Override
        public void updateReservation(UpdateReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
            try (Connection conn = getConnection()) {
                List<String> updates = new ArrayList<>();
                List<Object> params = new ArrayList<>();
                int paramIndex = 1;

                if (request.getClientId() > 0) {
                    updates.add("client_id = ?");
                    params.add(request.getClientId());
                }
                if (request.getChambreId() > 0) {
                    updates.add("chambre_id = ?");
                    params.add(request.getChambreId());
                }
                if (!request.getDateDebut().isEmpty()) {
                    updates.add("date_debut = ?");
                    params.add(request.getDateDebut());
                }
                if (!request.getDateFin().isEmpty()) {
                    updates.add("date_fin = ?");
                    params.add(request.getDateFin());
                }
                if (!request.getPreferences().isEmpty()) {
                    updates.add("preferences = ?");
                    params.add(request.getPreferences());
                }
                updates.add("updated_at = NOW()");
                params.add(request.getId());

                String sql = "UPDATE reservation SET " + String.join(", ", updates) + " WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                stmt.executeUpdate();

                GetReservationRequest getReq = GetReservationRequest.newBuilder().setId(request.getId()).build();
                getReservation(getReq, responseObserver);
            } catch (SQLException e) {
                responseObserver.onError(e);
            }
        }

        @Override
        public void deleteReservation(DeleteReservationRequest request, StreamObserver<DeleteReservationResponse> responseObserver) {
            try (Connection conn = getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM reservation WHERE id = ?");
                stmt.setLong(1, request.getId());
                stmt.executeUpdate();

                responseObserver.onNext(DeleteReservationResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            } catch (SQLException e) {
                responseObserver.onError(e);
            }
        }

        @Override
        public void listReservations(ListReservationsRequest request, StreamObserver<ListReservationsResponse> responseObserver) {
            try (Connection conn = getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT r.*, c.id as c_id, c.nom, c.prenom, c.email, c.telephone, " +
                    "ch.id as ch_id, ch.type, ch.prix, ch.disponible " +
                    "FROM reservation r " +
                    "JOIN client c ON r.client_id = c.id " +
                    "JOIN chambre ch ON r.chambre_id = ch.id"
                );
                ResultSet rs = stmt.executeQuery();

                ListReservationsResponse.Builder responseBuilder = ListReservationsResponse.newBuilder();
                while (rs.next()) {
                    responseBuilder.addReservations(buildReservationResponse(rs));
                }
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
            } catch (SQLException e) {
                responseObserver.onError(e);
            }
        }

        private ReservationResponse buildReservationResponse(ResultSet rs) throws SQLException {
            Client client = Client.newBuilder()
                    .setId(rs.getLong("c_id"))
                    .setNom(rs.getString("nom"))
                    .setPrenom(rs.getString("prenom"))
                    .setEmail(rs.getString("email"))
                    .setTelephone(rs.getString("telephone"))
                    .build();

            Chambre chambre = Chambre.newBuilder()
                    .setId(rs.getLong("ch_id"))
                    .setType(rs.getString("type"))
                    .setPrix(rs.getDouble("prix"))
                    .setDisponible(rs.getBoolean("disponible"))
                    .build();

            return ReservationResponse.newBuilder()
                    .setId(rs.getLong("id"))
                    .setClient(client)
                    .setChambre(chambre)
                    .setDateDebut(rs.getString("date_debut"))
                    .setDateFin(rs.getString("date_fin"))
                    .setPreferences(rs.getString("preferences"))
                    .build();
        }
    }
}

