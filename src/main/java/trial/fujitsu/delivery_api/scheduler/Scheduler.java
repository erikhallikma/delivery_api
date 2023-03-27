package trial.fujitsu.delivery_api.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import trial.fujitsu.delivery_api.database.Database;
import trial.fujitsu.delivery_api.model.Weather;
import trial.fujitsu.delivery_api.controller.WeatherController;

import java.util.List;

@Component
public class Scheduler {

    @Scheduled(cron = "${weather.import.cron:0 15 * * * *}")
    private void weatherImportTask() throws Exception {
        System.out.println("Importing weather data...");
        List<Weather> weatherList = WeatherController.getWeather();
        Database.insertWeatherData(weatherList);
    }
}
