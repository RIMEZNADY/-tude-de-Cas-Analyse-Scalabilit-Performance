from locust import HttpUser, task, between
import random

class ReservationUser(HttpUser):
    wait_time = between(1, 3)
    reservation_ids = []

    def on_start(self):
        """Création d'une réservation au démarrage"""
        response = self.client.post("/api/reservations", json={
            "clientId": 1,
            "chambreId": 1,
            "dateDebut": "2024-06-01",
            "dateFin": "2024-06-05",
            "preferences": "Test preferences"
        })
        if response.status_code == 201:
            data = response.json()
            self.reservation_ids.append(data.get("id"))

    @task(3)
    def get_reservation(self):
        """Consulter une réservation"""
        if self.reservation_ids:
            reservation_id = random.choice(self.reservation_ids)
            self.client.get(f"/api/reservations/{reservation_id}", name="/api/reservations/[id]")

    @task(2)
    def create_reservation(self):
        """Créer une réservation"""
        response = self.client.post("/api/reservations", json={
            "clientId": random.randint(1, 3),
            "chambreId": random.randint(1, 5),
            "dateDebut": "2024-06-01",
            "dateFin": "2024-06-05",
            "preferences": "Test preferences"
        }, name="/api/reservations")
        if response.status_code == 201:
            data = response.json()
            self.reservation_ids.append(data.get("id"))

    @task(1)
    def update_reservation(self):
        """Modifier une réservation"""
        if self.reservation_ids:
            reservation_id = random.choice(self.reservation_ids)
            self.client.put(f"/api/reservations/{reservation_id}", json={
                "clientId": random.randint(1, 3),
                "chambreId": random.randint(1, 5),
                "dateDebut": "2024-06-02",
                "dateFin": "2024-06-06",
                "preferences": "Updated preferences"
            }, name="/api/reservations/[id]")

    @task(1)
    def delete_reservation(self):
        """Supprimer une réservation"""
        if self.reservation_ids:
            reservation_id = self.reservation_ids.pop()
            self.client.delete(f"/api/reservations/{reservation_id}", name="/api/reservations/[id]")

