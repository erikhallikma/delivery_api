package trial.fujitsu.delivery_api.database;

import trial.fujitsu.delivery_api.model.City;
import trial.fujitsu.delivery_api.model.Weather;

import java.sql.*;
import java.util.List;

public class Database {

    /**
     * Inserts a list of weather data into the database
     * @param weatherList list of weather data, each object represents a row in the database. needed fields: wmo_code, station_name, air_temperature, wind_speed, phenomenon, timestamp
     * @throws SQLException if the database connection fails
     */
    public static void insertWeatherData(List<Weather> weatherList) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:weather_data", "sa", "");
        String query = "INSERT INTO weather_data (wmo_code, station_name, air_temperature, wind_speed, phenomenon, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        for (Weather weather : weatherList) {
            statement.setInt(1, weather.wmo_code());
            statement.setString(2, weather.station_name());
            statement.setDouble(3, weather.air_temperature());
            statement.setDouble(4, weather.wind_speed());
            statement.setString(5, weather.phenomenon());
            statement.setLong(6, weather.timestamp());
            statement.executeUpdate();
        }
        statement.close();
        connection.close();
    }

    /**
     * returns the air temperature in the given city at a time closest to the given timestamp
     * @param city city enum
     * @param timestamp UNIX timestamp.
     * @return air temperature as a double
     * @throws SQLException if the database connection fails
     */
    public static double getAirTemperature(City city, long timestamp) throws SQLException {

        int wmo_code = getWmoCode(city);

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:weather_data", "sa", "");
        String query = "SELECT air_temperature FROM weather_data WHERE wmo_code = ? AND timestamp <= ? ORDER BY timestamp DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, wmo_code);
        statement.setLong(2, timestamp);
        ResultSet rs = statement.executeQuery();

        double airTemperature = Double.NaN;
        if (rs.next()) {
            airTemperature = rs.getDouble("air_temperature");
        }

        rs.close();
        statement.close();
        connection.close();
        if (Double.isNaN(airTemperature)) {
            throw new SQLException("No weather data for this time period");
        }
        return airTemperature;
    }

    /**
     * returns the wind speed in the given city at a time closest to the given timestamp
     * @param city city enum
     * @param timestamp UNIX timestamp.
     * @return wind speed as a double
     * @throws SQLException if the database connection fails
     */
    public static double getWindSpeed(City city, long timestamp) throws SQLException {

        int wmo_code = getWmoCode(city);

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:weather_data", "sa", "");
        String query = "SELECT wind_speed FROM weather_data WHERE wmo_code = ? AND timestamp <= ? ORDER BY timestamp DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, wmo_code);
        statement.setLong(2, timestamp);
        ResultSet rs = statement.executeQuery();

        double windSpeed = Double.NaN;
        if (rs.next()) {
            windSpeed = rs.getDouble("wind_speed");
        }

        rs.close();
        statement.close();
        connection.close();
        if (Double.isNaN(windSpeed)) {
            throw new SQLException("No weather data for this time period");
        }
        return windSpeed;
    }

    /**
     * returns the phenomenon in the given city at a time closest to the given timestamp
     * @param city city enum
     * @param timestamp UNIX timestamp.
     * @return phenomenon as a String
     * @throws SQLException if the database connection fails
     */
    public static String getPhenomenon(City city, long timestamp) throws SQLException {

        int wmo_code = getWmoCode(city);
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:weather_data", "sa", "");
        String query = "SELECT phenomenon FROM weather_data WHERE wmo_code = ? AND timestamp <= ? ORDER BY timestamp DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, wmo_code);
        statement.setLong(2, timestamp);
        ResultSet rs = statement.executeQuery();

        String phenomenon = null;
        if (rs.next()) {
            phenomenon = rs.getString("phenomenon");
        }

        rs.close();
        statement.close();
        connection.close();
        if (phenomenon == null) {
            throw new SQLException("No weather data for this time period");
        }
        return phenomenon;
    }

    private static int getWmoCode(City city) {
        return switch (city) {
            case TALLINN -> 26038;
            case TARTU -> 26242;
            case PARNU -> 41803;
        };
    }
}
