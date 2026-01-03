const { ApolloServer, gql } = require('apollo-server-express');
const express = require('express');
const { Pool } = require('pg');

const app = express();

// Database connection
const pool = new Pool({
    host: process.env.DB_HOST || 'localhost',
    port: process.env.DB_PORT || 5432,
    database: process.env.DB_NAME || 'hotel_reservation',
    user: process.env.DB_USER || 'postgres',
    password: process.env.DB_PASSWORD || 'postgres'
});

// GraphQL Schema
const typeDefs = gql`
    type Client {
        id: ID!
        nom: String!
        prenom: String!
        email: String!
        telephone: String
    }

    type Chambre {
        id: ID!
        type: String!
        prix: Float!
        disponible: Boolean!
    }

    type Reservation {
        id: ID!
        client: Client!
        chambre: Chambre!
        dateDebut: String!
        dateFin: String!
        preferences: String
        createdAt: String
        updatedAt: String
    }

    input ReservationInput {
        clientId: ID!
        chambreId: ID!
        dateDebut: String!
        dateFin: String!
        preferences: String
    }

    input ReservationUpdateInput {
        clientId: ID
        chambreId: ID
        dateDebut: String
        dateFin: String
        preferences: String
    }

    type Query {
        reservation(id: ID!): Reservation
        reservations: [Reservation!]!
    }

    type Mutation {
        createReservation(input: ReservationInput!): Reservation!
        updateReservation(id: ID!, input: ReservationUpdateInput!): Reservation!
        deleteReservation(id: ID!): Boolean!
    }
`;

// Resolvers
const resolvers = {
    Query: {
        reservation: async (_, { id }) => {
            const result = await pool.query(
                'SELECT * FROM reservation WHERE id = $1',
                [id]
            );
            if (result.rows.length === 0) return null;
            return result.rows[0];
        },
        reservations: async () => {
            const result = await pool.query('SELECT * FROM reservation');
            return result.rows;
        }
    },
    Mutation: {
        createReservation: async (_, { input }) => {
            const { clientId, chambreId, dateDebut, dateFin, preferences } = input;
            const result = await pool.query(
                `INSERT INTO reservation (client_id, chambre_id, date_debut, date_fin, preferences, created_at, updated_at)
                 VALUES ($1, $2, $3, $4, $5, NOW(), NOW())
                 RETURNING *`,
                [clientId, chambreId, dateDebut, dateFin, preferences]
            );
            return result.rows[0];
        },
        updateReservation: async (_, { id, input }) => {
            const fields = [];
            const values = [];
            let paramCount = 1;

            if (input.clientId) {
                fields.push(`client_id = $${paramCount++}`);
                values.push(input.clientId);
            }
            if (input.chambreId) {
                fields.push(`chambre_id = $${paramCount++}`);
                values.push(input.chambreId);
            }
            if (input.dateDebut) {
                fields.push(`date_debut = $${paramCount++}`);
                values.push(input.dateDebut);
            }
            if (input.dateFin) {
                fields.push(`date_fin = $${paramCount++}`);
                values.push(input.dateFin);
            }
            if (input.preferences !== undefined) {
                fields.push(`preferences = $${paramCount++}`);
                values.push(input.preferences);
            }

            fields.push(`updated_at = NOW()`);
            values.push(id);

            const result = await pool.query(
                `UPDATE reservation SET ${fields.join(', ')} WHERE id = $${paramCount} RETURNING *`,
                values
            );
            return result.rows[0];
        },
        deleteReservation: async (_, { id }) => {
            await pool.query('DELETE FROM reservation WHERE id = $1', [id]);
            return true;
        }
    },
    Reservation: {
        client: async (parent) => {
            const result = await pool.query('SELECT * FROM client WHERE id = $1', [parent.client_id]);
            return result.rows[0];
        },
        chambre: async (parent) => {
            const result = await pool.query('SELECT * FROM chambre WHERE id = $1', [parent.chambre_id]);
            return result.rows[0];
        },
        dateDebut: (parent) => parent.date_debut,
        dateFin: (parent) => parent.date_fin,
        createdAt: (parent) => parent.created_at,
        updatedAt: (parent) => parent.updated_at
    },
    Chambre: {
        prix: (parent) => parseFloat(parent.prix)
    }
};

// Apollo Server
const server = new ApolloServer({
    typeDefs,
    resolvers,
    introspection: true,
    playground: true
});

async function startServer() {
    await server.start();
    server.applyMiddleware({ app, path: '/graphql' });

    app.get('/health', (req, res) => {
        res.json({ status: 'OK' });
    });

    const PORT = process.env.PORT || 4000;
    app.listen(PORT, () => {
        console.log(`GraphQL API running on http://localhost:${PORT}/graphql`);
    });
}

startServer().catch(err => {
    console.error('Error starting server:', err);
    process.exit(1);
});

