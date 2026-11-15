# ASTRA Monitor

**AI-assisted Astronaut Health Monitoring and Early-Warning System**  
NASA Space Apps Challenge 2026 · Health Monitoring Software

ASTRA Monitor is an experimental Mission Control dashboard for **simulated** astronaut health readings. It stores telemetry, evaluates risks, generates alerts, runs an anomaly-detection model, and displays findings through a live dashboard.

> **Prototype disclaimer:** All health readings are synthetic. Crew photos are illustrative, **not real astronaut medical records**. The risk thresholds and ML model are not clinically validated; this is not a medical diagnostic or flight-safety system.

## Features

- Heart rate, blood oxygen (SpO₂), temperature, and simulated stress monitoring.
- Java 21 / Spring Boot REST API with MySQL persistence and validation.
- Rule-based risk scores, levels, explanatory factors, and alerts.
- Experimental Python FastAPI + Isolation Forest anomaly detection trained on **simulated** data.
- WebSocket/STOMP live updates and REST polling fallback.
- Integrated V32 dashboard with dynamic health status, alerts, and **stored heart-rate history**.

## Architecture

```text
Synthetic readings / Postman
        |
        v
Spring Boot REST API (Java 21, port 8080)
        |
        +----> MySQL (readings, risks, alerts)
        +----> Rule-based risk assessment --> alerts
        +----> FastAPI / Isolation Forest (port 8000)
        +----> STOMP WebSocket (/ws -> /topic/health)
                                               |
                                               v
                               V32 Mission Control Dashboard
```

## Tech Stack

| Component | Technology |
| --- | --- |
| Frontend | HTML, CSS, JavaScript |
| Backend | Java 21, Spring Boot 4.1.1, Spring Data JPA/Hibernate |
| Database | MySQL |
| ML | Python, FastAPI, pandas, scikit-learn, Isolation Forest |
| Real-time | WebSocket / STOMP |
| Testing | Postman |

## Repository Structure

```text
ASTRA/
├── Frontend/
│   └── ASTRA_MONITOR_FINAL/   # Final V32 integrated frontend
├── backend/                   # Spring Boot API and WebSocket
├── ml-model/                  # FastAPI ML service and model
├── Simulator.py               # Single-pass test simulator
└── README.md
```

## Running Locally (Windows)

**Prerequisites:** Java 21, Python, and MySQL.

### 1. MySQL

Start MySQL and create the database if needed:

```sql
CREATE DATABASE IF NOT EXISTS astra;
```

The backend expects `jdbc:mysql://localhost:3306/astra`, username `root`, and a password provided through the `DB_PASSWORD` environment variable. **Never commit passwords.**

### 2. ML Service

In a terminal at the repository root:

```powershell
cd ml-model
python -m pip install -r requirements.txt
python -m uvicorn app:app --host 127.0.0.1 --port 8000
```

Check: `http://localhost:8000/health`

### 3. Spring Boot Backend

Open a new PowerShell terminal at the repository root:

```powershell
cd backend
$env:DB_PASSWORD = "YOUR_LOCAL_MYSQL_PASSWORD"
.\mvnw.cmd spring-boot:run
```

Alternatively, set `DB_PASSWORD` in IntelliJ's backend run configuration.

Check: `http://localhost:8080/api/astronauts`

### 4. Frontend

Open another terminal at the repository root:

```powershell
cd Frontend/ASTRA_MONITOR_FINAL
python run_frontend.py
```

Open **http://localhost:5500**. The local frontend server proxies `/api` to Spring Boot, while the browser connects to `ws://localhost:8080/ws` for STOMP messages.

## Main Endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| POST | `/api/health-readings` | Submit a simulated reading |
| GET | `/api/astronauts` | List astronauts |
| GET | `/api/astronauts/{id}/health` | Stored reading history |
| GET | `/api/astronauts/{id}/risk` | Latest risk assessment |
| GET | `/api/alerts` | List alerts |
| PATCH | `/api/alerts/{id}/status` | Change alert status |
| GET | `/api/dashboard/summary` | Dashboard summary |
| POST | `http://localhost:8000/predict` | Experimental ML anomaly prediction |

## Emergency Demo

Use Postman to send `POST http://localhost:8080/api/health-readings` with JSON body:

```json
{
  "astronautId": 1,
  "heartRate": 150,
  "spo2": 85,
  "temperature": 39.5,
  "stressLevel": 9
}
```

In local testing, this returned **201 Created**, and the dashboard showed updated readings, **CRITICAL risk (100/100)**, abnormal health indicators, and heart-rate history.

## Limitations and Project Status

- **Simulated telemetry only:** No actual astronaut physiological data are processed.
- Root `Simulator.py` is a **single-pass test script**; continuous emergency simulation is not yet verified. Its stress scale differs from the backend's tested 0–10 range.
- The graph is based on stored **BPM history**, not raw ECG samples.
- The ML output and rule-based risk assessment are **separate results**, not a validated combined diagnosis.
- The ML model is trained on **synthetic data**, not NASA physiological data.
- The application was demonstrated **locally**; permanent public deployment is not configured. Production hosting would require public backend/ML/database services and HTTPS/WSS configuration.

## Code and Collaboration

Latest integrated code: **[`devansh` branch](https://github.com/ayuhhsh/ASTRA/tree/devansh)**. The `main` branch may have other team changes pending review and merge.

---

*ASTRA Monitor — experimental mission-control telemetry and early-warning prototype.*
