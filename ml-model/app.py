import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Literal

import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, ConfigDict, Field, field_validator

BASE_DIR = Path(__file__).resolve().parent

MODEL_PATH = BASE_DIR / "isolation_forest.joblib"
CONFIG_PATH = BASE_DIR / "model_threshold.json"

FEATURES = [
    "heartRate",
    "spo2",
    "temperature",
    "stressLevel",
]

app = FastAPI(
    title="ASTRA-MONITOR AI API",
    version="0.3.0",
    description=(
        "Experimental astronaut telemetry anomaly detection. "
        "Not a validated medical diagnostic system."
    ),
)

# --------------------------------------------------
# LOAD MODEL AND MATCHING CONFIGURATION
# --------------------------------------------------

try:
    if not MODEL_PATH.exists():
        raise FileNotFoundError(f"Model not found: {MODEL_PATH}")

    if not CONFIG_PATH.exists():
        raise FileNotFoundError(f"Model configuration not found: {CONFIG_PATH}")

    model = joblib.load(MODEL_PATH)

    config = json.loads(
        CONFIG_PATH.read_text(encoding="utf-8")
    )

    THRESHOLD = float(config["threshold"])
    MODEL_FEATURES = config["features"]
    TRAINING_SOURCE = config.get(
        "training_data_source",
        "unknown",
    )

    if MODEL_FEATURES != FEATURES:
        raise ValueError(
            f"Model features {MODEL_FEATURES} do not match "
            f"API features {FEATURES}. "
            "Update the API schema before loading this model."
        )

    if TRAINING_SOURCE not in {"simulated", "nasa"}:
        raise ValueError(
            f"Unsupported or unknown training source: {TRAINING_SOURCE}"
        )

except Exception as exc:
    raise RuntimeError(
        "Could not load ASTRA model/configuration. "
        "Check train_model.py and model_threshold.json. "
        f"Details: {exc}"
    ) from exc


# --------------------------------------------------
# REQUEST VALIDATION
# --------------------------------------------------

class HealthData(BaseModel):
    model_config = ConfigDict(extra="forbid")

    astronautId: int = Field(ge=1, le=9999)

    heartRate: float = Field(
        ge=30,
        le=220,
        allow_inf_nan=False,
    )

    spo2: float = Field(
        ge=50,
        le=100,
        allow_inf_nan=False,
    )

    temperature: float = Field(
        ge=25,
        le=45,
        allow_inf_nan=False,
        description="Temperature in °C; measurement type must be verified.",
    )

    stressLevel: float = Field(
        ge=0,
        le=100,
        allow_inf_nan=False,
        description="Synthetic stress score, not a clinical measurement.",
    )

    recordedAt: str | None = None

    dataSource: Literal[
        "simulated",
        "nasa_historical",
    ] = "simulated"

    @field_validator("recordedAt")
    @classmethod
    def validate_timestamp(cls, value):
        if value is None:
            return value

        try:
            datetime.fromisoformat(
                value.replace("Z", "+00:00")
            )
        except ValueError as exc:
            raise ValueError(
                "recordedAt must be a valid ISO 8601 timestamp."
            ) from exc

        return value


# --------------------------------------------------
# API STATUS
# --------------------------------------------------

@app.get("/")
def home():
    return {
        "message": "ASTRA-MONITOR AI API is running",
        "modelLoaded": True,
        "model": config.get("model", "unknown"),
        "features": FEATURES,
        "threshold": THRESHOLD,
        "thresholdMethod": config.get(
            "threshold_method",
            "unknown",
        ),
        "trainingDataSource": TRAINING_SOURCE,
        "docs": "/docs",
    }


@app.get("/health")
def health():
    return {
        "status": "ok",
        "modelLoaded": True,
        "trainingDataSource": TRAINING_SOURCE,
    }


# --------------------------------------------------
# ANOMALY PREDICTION
# --------------------------------------------------

@app.post("/predict")
def predict(data: HealthData):

    # Do not silently use a synthetic-trained model
    # as if it were trained on NASA data, or vice versa.
    compatible_sources = {
        "simulated": "simulated",
        "nasa_historical": "nasa",
    }

    expected_training_source = compatible_sources[
        data.dataSource
    ]

    if TRAINING_SOURCE != expected_training_source:
        raise HTTPException(
            status_code=409,
            detail={
                "message": (
                    "The requested data source is incompatible "
                    "with the loaded model's training source."
                ),
                "requestedDataSource": data.dataSource,
                "trainingDataSource": TRAINING_SOURCE,
                "action": (
                    "Use a compatible dataset/model combination. "
                    "NASA historical data requires an appropriately "
                    "trained model and matching feature schema."
                ),
            },
        )

    try:
        features = pd.DataFrame(
            [[
                data.heartRate,
                data.spo2,
                data.temperature,
                data.stressLevel,
            ]],
            columns=FEATURES,
        )

        score = float(
            model.decision_function(features)[0]
        )

        anomaly = score < THRESHOLD

        if TRAINING_SOURCE == "simulated":
            disclaimer = (
                "Experimental output from a model trained on "
                "synthetic data. Not a medical diagnosis, validated "
                "astronaut-health assessment, or flight-safety instruction."
            )
        else:
            disclaimer = (
                "Experimental anomaly detection using a model trained "
                "on NASA-sourced data. This does not establish clinical "
                "validity, medical risk, or flight-safety suitability."
            )

        return {
            "astronautId": data.astronautId,
            "recordedAt": (
                data.recordedAt
                or datetime.now(timezone.utc).isoformat()
            ),
            "dataSource": data.dataSource,
            "trainingDataSource": TRAINING_SOURCE,
            "anomalyDetected": bool(anomaly),
            "riskLevel": (
                "REVIEW" if anomaly else "NORMAL"
            ),
            "anomalyScore": round(score, 6),
            "threshold": THRESHOLD,
            "thresholdMethod": config.get(
                "threshold_method",
                "unknown",
            ),
            "message": (
                "Unusual pattern detected by the experimental model; "
                "review the reading."
                if anomaly
                else
                "No anomaly detected by the experimental model "
                "for this reading."
            ),
            "disclaimer": disclaimer,
        }

    except Exception as exc:
        raise HTTPException(
            status_code=500,
            detail=f"Prediction failed: {exc}",
        ) from exc