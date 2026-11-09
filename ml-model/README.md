# ASTRA-MONITOR — File Handover

## Files and Their Purpose

- app.py — FastAPI backend that receives astronaut health readings and returns anomaly-detection results.
- isolation_forest.joblib — Trained Isolation Forest model used to detect unusual readings.
- model_threshold.json — Contains the threshold configuration used for anomaly detection.
- integrate_simulator.py — Sends simulated astronaut readings to the backend API and displays prediction results.
- Simulator.py — Generates changing synthetic health readings for four astronauts.

## Steps for Integration

1. Keep `app.py`, `isolation_forest.joblib`, and `model_threshold.json` in the same folder.
2. Install the required Python dependencies. (Open requirements.txt, for this step)
3. Start the backend:
   "python -m uvicorn app:app --reload"
   
4. Open "http://127.0.0.1:8000/docs" to test the API.
5. Run the simulator integration in another terminal:
   "python integrate_simulator.py"

Note: The current model uses synthetic training data. NASA data integration is still pending.

