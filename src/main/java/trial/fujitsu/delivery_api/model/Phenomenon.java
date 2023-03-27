package trial.fujitsu.delivery_api.model;

public enum Phenomenon {
    SNOW,
    RAIN,
    EXTREME,
    NORMAL;

    public static Phenomenon categorize(String phenomenon) {
        if (phenomenon.contains("snow") || phenomenon.contains("sleet")) {
            return SNOW;
        } else if (phenomenon.contains("rain") || phenomenon.contains("shower")) {
            return RAIN;
        } else if (phenomenon.contains("glaze") || phenomenon.contains("thunder") || phenomenon.contains("hail")) {
            return Phenomenon.EXTREME;
        } else {
            return Phenomenon.NORMAL;
        }
    }
}
