import random
import time
import json

astronauts = {
    1: {
        "name": "AST-001",
        "heart_rate": 80,
        "spo2": 98,
        "temperature": 36.8,
        "stress_level": 25
    },

    2: {
        "name": "AST-002",
        "heart_rate": 75,
        "spo2": 97,
        "temperature": 36.7,
        "stress_level": 30
    },

    3: {
        "name": "AST-003",
        "heart_rate": 84,
        "spo2": 99,
        "temperature": 36.9,
        "stress_level": 20
    },

    4: {
        "name": "AST-004",
        "heart_rate": 78,
        "spo2": 98,
        "temperature": 36.8,
        "stress_level": 35
    }
}


while True:

    for astronaut_id, astronaut in astronauts.items():

        # Generate small changes
        astronaut["heart_rate"] += random.randint(-2, 2)

        astronaut["spo2"] += random.choice([-1, 0, 0, 0, 1])

        astronaut["temperature"] += random.uniform(-0.1, 0.1)

        astronaut["stress_level"] += random.randint(-2, 2)

        # Keep values within simulation ranges
        astronaut["heart_rate"] = max(
            60, min(100, astronaut["heart_rate"])
        )

        astronaut["spo2"] = max(
            94, min(100, astronaut["spo2"])
        )

        astronaut["temperature"] = max(
            36.0, min(38.0, astronaut["temperature"])
        )

        astronaut["stress_level"] = max(
            0, min(100, astronaut["stress_level"])
        )

        # Create JSON
        health_data = {
            "astronautId": astronaut_id,
            "heartRate": astronaut["heart_rate"],
            "spo2": astronaut["spo2"],
            "temperature": round(
                astronaut["temperature"], 1
            ),
            "stressLevel": astronaut["stress_level"]
        }

        print(json.dumps(health_data))

    time.sleep(1)