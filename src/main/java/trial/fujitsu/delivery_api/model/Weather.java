package trial.fujitsu.delivery_api.model;



public record Weather( int wmo_code, String station_name, double air_temperature, double wind_speed, String phenomenon, long timestamp) {
}
