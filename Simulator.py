import random
import time
import json
import urllib.request

API_URL = "http://localhost:8080/api/health-readings"

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


def send_health_data(health_data):

    data = json.dumps(health_data).encode("utf-8")

    request = urllib.request.Request(
        API_URL,
        data=data,
        headers={
            "Content-Type": "application/json"
        },
        method="POST"
    )

    try:
        with urllib.request.urlopen(request) as response:
            result = response.read().decode("utf-8")
            print("Sent:", result)

    except Exception as e:
        print("Error:", e)


# TEST MODE
# Only one reading for each astronaut
for astronaut_id, astronaut in astronauts.items():

    astronaut["heart_rate"] += random.randint(-2, 2)

    astronaut["spo2"] += random.choice([-1, 0, 0, 0, 1])

    astronaut["temperature"] += random.uniform(-0.1, 0.1)

    astronaut["stress_level"] += random.randint(-2, 2)

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

    health_data = {
        "astronautId": astronaut_id,
        "heartRate": astronaut["heart_rate"],
        "spo2": astronaut["spo2"],
        "temperature": round(
            astronaut["temperature"], 1
        ),
        "stressLevel": astronaut["stress_level"]
    }

    send_health_data(health_data)

    time.sleep(1)