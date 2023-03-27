-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE weather_data (
    wmo_code INT,
    station_name VARCHAR(255),
    air_temperature DOUBLE,
    wind_speed DOUBLE,
    phenomenon VARCHAR(255),
    timestamp INT
);
