import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const createReservationTrend = new Trend('create_reservation_duration');
const getReservationTrend = new Trend('get_reservation_duration');

export const options = {
  stages: [
    { duration: '30s', target: 10 },
    { duration: '1m', target: 100 },
    { duration: '30s', target: 500 },
    { duration: '1m', target: 1000 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    errors: ['rate<0.01'],
  },
};

const BASE_URL = __ENV.API_URL || 'http://localhost:8080';

export default function () {
  // Create Reservation
  const createPayload = JSON.stringify({
    clientId: 1,
    chambreId: 1,
    dateDebut: '2024-06-01',
    dateFin: '2024-06-05',
    preferences: 'Test preferences',
  });

  const createParams = {
    headers: { 'Content-Type': 'application/json' },
    tags: { name: 'CreateReservation' },
  };

  const createRes = http.post(`${BASE_URL}/api/reservations`, createPayload, createParams);
  const createSuccess = check(createRes, {
    'create status is 201': (r) => r.status === 201,
  });
  errorRate.add(!createSuccess);
  createReservationTrend.add(createRes.timings.duration);

  if (createRes.status === 201) {
    const reservation = JSON.parse(createRes.body);
    const reservationId = reservation.id;

    // Get Reservation
    const getRes = http.get(`${BASE_URL}/api/reservations/${reservationId}`, {
      tags: { name: 'GetReservation' },
    });
    const getSuccess = check(getRes, {
      'get status is 200': (r) => r.status === 200,
    });
    errorRate.add(!getSuccess);
    getReservationTrend.add(getRes.timings.duration);

    // Update Reservation
    const updatePayload = JSON.stringify({
      clientId: 1,
      chambreId: 2,
      dateDebut: '2024-06-02',
      dateFin: '2024-06-06',
      preferences: 'Updated preferences',
    });

    const updateRes = http.put(`${BASE_URL}/api/reservations/${reservationId}`, updatePayload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'UpdateReservation' },
    });
    check(updateRes, {
      'update status is 200': (r) => r.status === 200,
    });

    // Delete Reservation
    const deleteRes = http.del(`${BASE_URL}/api/reservations/${reservationId}`, null, {
      tags: { name: 'DeleteReservation' },
    });
    check(deleteRes, {
      'delete status is 204': (r) => r.status === 204,
    });
  }

  sleep(1);
}

