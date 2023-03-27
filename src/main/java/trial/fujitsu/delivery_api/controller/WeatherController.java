package trial.fujitsu.delivery_api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import trial.fujitsu.delivery_api.model.Weather;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherController {

    private static RestTemplate restTemplate;

    private WeatherController() {
        restTemplate = new RestTemplate();
    }

    /**
     * Gets weather data from the Estonian Meteorological Institute
     * @return List of Weather objects, containing weather data for Tallinn-Harku, Tartu-Tõravere and Pärnu
     * @throws Exception if the data cannot be retrieved
     */
    public static List<Weather> getWeather() throws Exception {
        String url = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
        String xmlData = restTemplate.getForObject(url, String.class);
        if (xmlData == null) {
            throw new Exception("Cannot get weather data");
        }

        //parse xml data
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlData));
        Document document = builder.parse(inputSource);
        long timestamp = Long.parseLong((document.getElementsByTagName("observations").item(0).getAttributes().getNamedItem("timestamp").getNodeValue()));
        NodeList nodeList = document.getElementsByTagName("station");
        List<Weather> weatherList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList childNodes = nodeList.item(i).getChildNodes();
            String stationName = childNodes.item(1).getTextContent();
            if (stationName.equals("Tallinn-Harku") || stationName.equals("Tartu-Tõravere") || stationName.equals("Pärnu")) {
                //node indexes seem to always stay the same, so I just counted them manually instead of
                //looping over every element and checking for the correct name
                int wmoCode = Integer.parseInt(childNodes.item(3).getTextContent());
                double airTemp = Double.parseDouble(childNodes.item(19).getTextContent());
                double windSpeed = Double.parseDouble(childNodes.item(23).getTextContent());
                String phenomenon = childNodes.item(9).getTextContent();
                weatherList.add(new Weather(wmoCode, stationName, airTemp, windSpeed, phenomenon, timestamp));
            }
        }
        return weatherList;
    }
}
