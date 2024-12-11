### Fields exercice 8

temperature_readings => sensor_id, zone_id, temperature, timestamp
humidity_readings => sensor_id, zone_id, humidity, timestamp

### Correction Exercice 8

1. Création des streams

CREATE STREAM temperature_readings (
  sensor_id INT,
  zone_id INT,
  temperature DOUBLE,
  timestamp BIGINT
) WITH (
  KAFKA_TOPIC='temperature_topic',
  VALUE_FORMAT='JSON',
  partitions=3
);

CREATE STREAM humidity_readings (
  sensor_id INT,
  zone_id INT,
  humidity DOUBLE,
  timestamp BIGINT
) WITH (
  KAFKA_TOPIC='humidity_topic',
  VALUE_FORMAT='JSON',
  partitions=3
);

2. Insertion de données
INSERT INTO temperature_readings (sensor_id, zone_id, temperature, timestamp) VALUES (1, 101, 22.5, 1627847282000);
INSERT INTO temperature_readings (sensor_id, zone_id, temperature, timestamp) VALUES (2, 102, 23.0, 1627847342000);
INSERT INTO temperature_readings (sensor_id, zone_id, temperature, timestamp) VALUES (3, 101, 21.8, 1627847402000);

INSERT INTO humidity_readings (sensor_id, zone_id, humidity, timestamp) VALUES (1, 101, 45.0, 1627847282000);
INSERT INTO humidity_readings (sensor_id, zone_id, humidity, timestamp) VALUES (2, 102, 50.0, 1627847342000);
INSERT INTO humidity_readings (sensor_id, zone_id, humidity, timestamp) VALUES (3, 101, 48.0, 1627847402000);

3. Création de jointure
CREATE STREAM sensor_data AS
SELECT t.sensor_id,
       t.zone_id,
       t.temperature,
       h.humidity,
       t.timestamp
FROM temperature_readings t
JOIN humidity_readings h WITHIN 5 MINUTES
ON t.sensor_id = h.sensor_id
AND t.timestamp = h.timestamp;

4. Création d'une table d'aggrégation
CREATE TABLE avg_sensor_data AS
SELECT zone_id,
       AVG(temperature) AS avg_temperature,
       AVG(humidity) AS avg_humidity,
       TUMBLINGWINDOW AS window_start
FROM sensor_data
WINDOW TUMBLING (SIZE 5 MINUTES)
GROUP BY zone_id;