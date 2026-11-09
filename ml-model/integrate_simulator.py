import argparse
import random
import time
from datetime import datetime, timezone

import requests

API_URL = "http://127.0.0.1:8000/predict"

ASTRONAUTS = {
    1: {"heartRate": 80, "spo2": 98, "temperature": 36.8, "stressLevel": 25},
    2: {"heartRate": 75, "spo2": 97, "temperature": 36.7, "stressLevel": 30},
    3: {"heartRate": 84, "spo2": 99, "temperature": 36.9, "stressLevel": 20},
    4: {"heartRate": 78, "spo2": 98, "temperature": 36.8, "stressLevel": 35},
}

def create_reading(astronaut_id, baseline, inject_anomaly=False):
    if inject_anomaly:
        # Deliberately synthetic test case to verify the software path.
        return {
            "astronautId": astronaut_id,
            "heartRate": random.randint(135, 150),
            "spo2": random.randint(86, 91),
            "temperature": round(random.uniform(38.5, 39.2), 1),
            "stressLevel": random.randint(80, 98),
            "recordedAt": datetime.now(timezone.utc).isoformat(),
            "dataSource": "simulated"
        }

    return {
        "astronautId": astronaut_id,
        "heartRate": max(60, min(110, baseline["heartRate"] + random.randint(-3, 3))),
        "spo2": max(94, min(100, baseline["spo2"] + random.choice([-1, 0, 0, 0, 1]))),
        "temperature": round(max(36.0, min(37.5, baseline["temperature"] + random.uniform(-0.2, 0.2))), 1),
        "stressLevel": max(0, min(65, baseline["stressLevel"] + random.randint(-4, 4))),
        "recordedAt": datetime.now(timezone.utc).isoformat(),
        "dataSource": "simulated"
    }

def main():
    parser = argparse.ArgumentParser(description="ASTRA synthetic telemetry -> FastAPI")
    parser.add_argument("--api-url", default=API_URL)
    parser.add_argument("--interval", type=float, default=1.0, help="Seconds between rounds")
    parser.add_argument("--anomaly-every", type=int, default=20, help="Inject one demo anomaly every N rounds; 0 disables injection")
    args = parser.parse_args()

    if args.interval <= 0:
        parser.error("--interval must be greater than 0")
    if args.anomaly_every < 0:
        parser.error("--anomaly-every must be 0 or greater")

    round_number = 0
    print("ASTRA-MONITOR synthetic simulator started. Press Ctrl+C to stop.")
    print("All readings are synthetic. Injected anomaly scenarios are software tests, not medical events.")
    try:
        while True:
            round_number += 1
            for astronaut_id, baseline in ASTRONAUTS.items():
                inject = args.anomaly_every > 0 and round_number % args.anomaly_every == 0 and astronaut_id == 1
                payload = create_reading(astronaut_id, baseline, inject_anomaly=inject)
                try:
                    response = requests.post(args.api_url, json=payload, timeout=5)
                    response.raise_for_status()
                    result = response.json()
                    tag = "INJECTED TEST" if inject else "SIMULATED"
                    print(
                        f"{tag} | AST-{astronaut_id:03d} | "
                        f"HR {payload['heartRate']} bpm | SpO2 {payload['spo2']}% | "
                        f"Temp {payload['temperature']} C | Stress {payload['stressLevel']} | "
                        f"AI {result['riskLevel']} | score {result['anomalyScore']}"
                    )
                except requests.RequestException as exc:
                    print(f"AST-{astronaut_id:03d}: API request failed: {exc}")
            print("-" * 100)
            time.sleep(args.interval)
    except KeyboardInterrupt:
        print("\nSimulator stopped.")

if __name__ == "__main__":
    main()
