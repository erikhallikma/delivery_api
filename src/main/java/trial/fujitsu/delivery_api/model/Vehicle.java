package trial.fujitsu.delivery_api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Vehicle {
    CAR("Car"),
    SCOOTER("Scooter"),
    BIKE("Bike");

    private final String value;

    Vehicle(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Vehicle fromValue(String value) {
        for (Vehicle e : Vehicle.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle: " + value);
    }
}

