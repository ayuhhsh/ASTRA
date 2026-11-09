
"""
ASTRA-MONITOR preprocessing and feature validation.

Purpose:
1. Map documented source columns to canonical ASTRA features.
2. Validate numeric model inputs.
3. Reject missing, infinite, duplicate, or incompatible features.

NASA column mappings must only be configured after reviewing
the official dataset documentation and measurement units.
"""

from __future__ import annotations

import numpy as np
import pandas as pd


ASTRA_FEATURES = [
    "heartRate",
    "spo2",
    "temperature",
    "stressLevel",
]


def map_features(
    df: pd.DataFrame,
    column_mapping: dict[str, str],
    required_features: list[str] | None = None,
) -> pd.DataFrame:
    """
    Convert documented source columns into ASTRA feature names.

    Example for the current simulated dataset:

        {
            "heartRate": "heartRate",
            "spo2": "spo2",
            "temperature": "temperature",
            "stressLevel": "stressLevel"
        }

    Do not use this example as a NASA mapping without
    verifying the NASA data dictionary and units.
    """

    if not isinstance(df, pd.DataFrame):
        raise TypeError("Input must be a pandas DataFrame.")

    if df.empty:
        raise ValueError("Source dataset is empty.")

    if not df.columns.is_unique:
        raise ValueError(
            "Duplicate source column names detected."
        )

    if not column_mapping:
        raise ValueError(
            "No feature mappings provided."
        )

    # Validate canonical feature names.
    invalid_features = [
        feature
        for feature in column_mapping
        if feature not in ASTRA_FEATURES
    ]

    if invalid_features:
        raise ValueError(
            f"Unsupported ASTRA features: {invalid_features}"
        )

    # One source column must not represent multiple features.
    source_columns = list(column_mapping.values())

    if len(source_columns) != len(set(source_columns)):
        raise ValueError(
            "A source column cannot map to multiple ASTRA features."
        )

    # Required features must be explicitly mapped.
    if required_features is None:
        required_features = list(column_mapping.keys())

    if len(required_features) != len(set(required_features)):
        raise ValueError(
            "Duplicate required features detected."
        )

    invalid_required = [
        feature
        for feature in required_features
        if feature not in ASTRA_FEATURES
    ]

    if invalid_required:
        raise ValueError(
            f"Unsupported required features: {invalid_required}"
        )

    missing_features = [
        feature
        for feature in required_features
        if feature not in column_mapping
    ]

    if missing_features:
        raise ValueError(
            f"Required features have no source mapping: "
            f"{missing_features}"
        )

    # Check that all mapped source columns exist.
    missing_sources = [
        source
        for source in source_columns
        if source not in df.columns
    ]

    if missing_sources:
        raise ValueError(
            f"Source columns not found: {missing_sources}"
        )

    # Create a separate feature dataframe.
    result = pd.DataFrame(index=df.index)

    for canonical, source in column_mapping.items():

        result[canonical] = pd.to_numeric(
            df[source],
            errors="coerce"
        )

    # Validate missing and non-numeric measurements.
    selected = result[required_features]

    missing_counts = selected.isna().sum()
    missing_counts = missing_counts[
        missing_counts > 0
    ]

    if not missing_counts.empty:
        raise ValueError(
            "Missing or non-numeric measurements detected: "
            f"{missing_counts.to_dict()}. "
            "Define a documented missing-data policy."
        )

    validate_numeric_features(selected)

    return selected.copy()


def validate_numeric_features(
    X: pd.DataFrame
) -> None:
    """
    Validate features before model training or prediction.
    """

    if not isinstance(X, pd.DataFrame):
        raise TypeError(
            "Model input must be a pandas DataFrame."
        )

    if X.empty:
        raise ValueError(
            "No rows or features available."
        )

    if len(X.columns) == 0:
        raise ValueError(
            "No model features supplied."
        )

    if not X.columns.is_unique:
        raise ValueError(
            "Duplicate model feature names detected."
        )

    non_numeric = [
        column
        for column in X.columns
        if not pd.api.types.is_numeric_dtype(X[column])
    ]

    if non_numeric:
        raise TypeError(
            f"Non-numeric features detected: {non_numeric}"
        )

    values = X.to_numpy(dtype=float)

    if not np.isfinite(values).all():

        invalid_columns = X.columns[
            ~np.isfinite(values).all(axis=0)
        ].tolist()

        raise ValueError(
            "Infinite or invalid numerical values detected "
            f"in features: {invalid_columns}"
        )


if __name__ == "__main__":

    print("ASTRA-MONITOR preprocessing module")
    print("-----------------------------------")

    print("Supported canonical features:")

    for feature in ASTRA_FEATURES:
        print(f" - {feature}")

    print("\nModule loaded successfully.")
    print(
        "NASA feature mappings must be configured "
        "after reviewing the official data dictionary."
    )
