package trial.fujitsu.delivery_api.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import trial.fujitsu.delivery_api.database.Database;

import java.sql.SQLException;
public class Delivery {
    private double RBF;
    private double ATEF;
    private double WSEF;
    private double WPEF;
    private final long timestamp;


    private Delivery(City city, Vehicle vehicle, double RBF, double ATEF, double WSEF, double WPEF, long timestamp) {
        if (city == null || vehicle == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and vehicle must be specified");
        }
        this.timestamp = currentTimestamp(timestamp);
        switch (vehicle) {
            case CAR -> {
                this.RBF = 3.0;
                this.ATEF = 0;
                this.WSEF = 0;
                this.WPEF = 0;
            }
            case SCOOTER -> {
                this.RBF = 2.5;
                this.ATEF = this.calculateATEF(city);
                this.WSEF = 0;
                this.WPEF = this.calculateWPEF(city);
            }
            case BIKE -> {
                this.RBF = 2.0;
                this.ATEF = this.calculateATEF(city);
                this.WSEF = this.calculateWSEF(city);
                this.WPEF = this.calculateWPEF(city);
            }
        }

        switch (city) {
            case TALLINN -> this.RBF += 1;
            case TARTU -> this.RBF += 0.5;
            case PARNU -> this.RBF += 0;
        }

        if (RBF != 0) {
            this.RBF = RBF;
        }
        if (ATEF != 0) {
            this.ATEF = ATEF;
        }
        if (WSEF != 0) {
            this.WSEF = WSEF;
        }
        if (WPEF != 0) {
            this.WPEF = WPEF;
        }
    }

    private double calculateATEF(City city) {
        double airTemperature;
        try {
            airTemperature = Database.getAirTemperature(city, timestamp);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No weather data for this time period");
        }
        if (airTemperature < -10) {
            return 1;
        } else if (airTemperature < 0) {
            return 0.5;
        } else {
            return 0;
        }
    }

    private double calculateWSEF(City city) {
        double windSpeed;
        try {
            windSpeed = Database.getWindSpeed(city, timestamp);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No weather data for this time period");
        }
        if (windSpeed >= 10 && windSpeed <= 20) {
            return 0.5;
        } else if (windSpeed > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Usage of selected vehicle type is not allowed in these weather conditions");
        } else {
            return 0;
        }
    }

    private double calculateWPEF(City city) {
        String weatherPhenomenon;
        try {
            weatherPhenomenon = Database.getPhenomenon(city, timestamp);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No weather data for this time period");
        }
        Phenomenon phenomenon = Phenomenon.categorize(weatherPhenomenon);
        switch (phenomenon) {
            case SNOW -> {
                return 1;
            }
            case RAIN -> {
                return 0.5;
            }
            case EXTREME -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usage of selected vehicle type is not allowed in these weather conditions");
            default -> {
                return 0;
            }
        }
    }

    private static long currentTimestamp(long timestamp) {
        if (timestamp == 0) {
            return System.currentTimeMillis() / 1000;
        }
        return timestamp;
    }

    /**
     * Calculates delivery price based on the formula: RBF + ATEF + WSEF + WPEF
     *
     * @return delivery price
     */
    public double getDeliveryPrice() {
        return this.RBF + this.ATEF + this.WSEF + this.WPEF;
    }

}
