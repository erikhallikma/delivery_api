package trial.fujitsu.delivery_api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum City {
    TALLINN("Tallinn"),
    TARTU("Tartu"),
    PARNU("Pärnu");

    private final String value;

    City(String value) {
        this.value = value;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    private static City fromValue(String value) {
        for (City e : City.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid city:" + value);
    }
}
