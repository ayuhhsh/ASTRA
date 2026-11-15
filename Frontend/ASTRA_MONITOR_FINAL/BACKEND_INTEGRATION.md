# ASTRA MONITOR V29 — Backend telemetry contract

The frontend does not generate vital readings or ECG data. It polls `GET http://localhost:8000/api/telemetry` every 2.8 seconds. Override the base URL by defining `window.ASTRA_API_BASE` before the main script in `index.html`.

Example JSON response:

```json
{
  "timestamp": "2026-11-15T12:00:00Z",
  "crew": {
    "reid-wiseman": {"hr": 70, "spo2": 98, "temp": 36.7, "stress": 22, "ecg": [0, 0.1, -0.2, 0.8, -0.3, 0]},
    "victor-glover": {"hr": 72, "spo2": 99, "temp": 36.6, "stress": 19, "ecg": [0, 0.1, -0.2, 0.8, -0.3, 0]},
    "christina-koch": {"hr": 65, "spo2": 98, "temp": 36.8, "stress": 27, "ecg": [0, 0.1, -0.2, 0.8, -0.3, 0]},
    "jeremy-hansen": {"hr": 70, "spo2": 98, "temp": 36.5, "stress": 22, "ecg": [0, 0.1, -0.2, 0.8, -0.3, 0]}
  }
}
```

`ecg` must be an array of normalized ECG amplitude samples in the range -1 to 1, ordered oldest to newest. It must come from the backend simulator; the frontend does not derive a waveform from BPM. All four crew members are rendered in Mission Status. The selected crew member's values and ECG are rendered in Health Dashboard. On backend failure, the last valid backend values are retained; without prior values, placeholders are shown.

If frontend and FastAPI run on different origins, enable CORS for your frontend origin. For local testing, run the frontend using a local HTTP server (not `file://`).

The API is an integration contract, not an implemented backend service. All medical values are simulated, not real NASA crew health data.
