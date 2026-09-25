USE astra_readings;

CREATE TABLE health_readings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    astronaut_id INT NOT NULL,
    heart_rate INT NOT NULL,
    spo2 INT NOT NULL,
    temperature DOUBLE,
    stress_level INT,
    recorded_at DATETIME DEFAULT CURRENT_TIMESTAMP
);