ASTRA MONITOR V32 — FINAL SPRING BOOT INTEGRATION

This is teammate LAST.zip V32 design, with actual Spring Boot backend integration.
Run MySQL + Spring Boot at http://localhost:8080. FastAPI ML optional at localhost:8000.
In this folder: python run_frontend.py
Then open http://localhost:5500 in Chrome, hard refresh Ctrl+Shift+R.

Backend endpoints used:
GET /api/astronauts
GET /api/astronauts/{id}/health
GET /api/astronauts/{id}/risk
GET /api/alerts
WebSocket ws://localhost:8080/ws STOMP SUBSCRIBE /topic/health

Four display cards use NASA Artemis II portraits only as illustration.
Backend roster IDs 1..4 are mapped in order to four display slots; NOT real NASA astronaut health data.
No fake frontend vitals or ECG: the graph now displays genuine stored heart-rate history (the backend does not provide raw ECG).
Stress backend 0..10 mapped to frontend 0..100.
Live banner shows connectivity. Alerts and latest risk based on Spring Boot APIs.

POST TEST (Postman):
http://localhost:8080/api/health-readings
{"astronautId":1,"heartRate":145,"spo2":87,"temperature":39.2,"stressLevel":9}

For production: ASTRA_API_BASE must be set before page scripts to the HTTPS backend URL (or serve both at one origin). Set window.ASTRA_WS_URL to wss://your-backend/ws. Configure server origins; production requires HTTPS/WSS.

Unverified until real services are running: WebSocket message delivery, latest risk timing, alert rendering. This ZIP is static/code integration not an end-to-end live test.

V32 FINAL FRONTEND PATCH (15 November 2026)
- Heart-rate chart is now a trend of up to 24 genuine stored BPM readings from
  GET /api/astronauts/{id}/health and current WebSocket events.
- Chart labels say RECENT STORED READINGS, not 10-second ECG. No raw ECG is invented.
- Health Status is dynamically derived from the same prototype rule thresholds used
  by RiskAssessmentService: HR <50 or >120, SpO2 <90, temperature <35 or >38.5,
  stress >=8 (backend 0..10; displayed 0..100). These are DEMO thresholds,
  NOT clinical measurements or validated medical limits.
- The fourth row is now Stress response instead of a fake sleep/recovery status;
  the backend does not supply a sleep measurement.
- Code/DOM smoke-tested with mocked backend readings: 85, 145, 150 BPM; risk
  CRITICAL 100/100; health statuses High/Low SpO2/High/High; no JavaScript errors.
- Live Windows deployment still needs testing against the user's actual backend.
