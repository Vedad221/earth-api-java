package si.telekom.potres.service;

import si.telekom.potres.configuration.PotresApiConfig;
import si.telekom.potres.model.Potres;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.telekom.potres.model.Vreme;



@Slf4j
@Service
public class PotresiService {
    private final RestTemplate restTemplate;
    private final PotresApiConfig potresApiConfig;

    @Autowired
    public PotresiService(RestTemplate restTemplate, PotresApiConfig potresApiConfig) {
        this.restTemplate = restTemplate;
        this.potresApiConfig = potresApiConfig;

    }



    @CircuitBreaker(name = "potresi", fallbackMethod = "fallbackZaPotrese")
    public Potres najdiZadnjiMesec() {
        String url = potresApiConfig.getMesec();


        try {

            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);


            JsonNode features = rootNode.path("features");

            double maxMagnitude = 0;
            Potres najhujsiPotres = null;


            for (JsonNode feature : features) {
                JsonNode properties = feature.path("properties");
                double magnitude = properties.path("mag").asDouble();

                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;


                    String kraj = properties.path("place").asText();


                    JsonNode geometry = feature.path("geometry");
                    JsonNode coordinates = geometry.path("coordinates");


                    double longitude = coordinates.get(0).asDouble();
                    double latitude = coordinates.get(1).asDouble();
                    double globina = coordinates.get(2).asDouble();

                    String geoLokacija = latitude + "," + longitude;

                    najhujsiPotres = new Potres(kraj, geoLokacija, globina);
                }
            }

            return najhujsiPotres;
        } catch (Exception e) {
            log.error("Napaka pri pridobitvi podatkov za zadnji mesec", e);
            return new Potres("N/A", "N/A", 0.0);
        }
    }
    @CircuitBreaker(name = "potresi", fallbackMethod = "fallbackZaPotrese")
    public Potres najdiZadnjiTeden() {
        String url = potresApiConfig.getTeden();

        try {

            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);


            JsonNode features = rootNode.path("features");

            double maxMagnitude = 0;
            Potres najhujsiPotres = null;


            for (JsonNode feature : features) {
                JsonNode properties = feature.path("properties");
                double magnitude = properties.path("mag").asDouble();

                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;


                    String kraj = properties.path("place").asText();


                    JsonNode geometry = feature.path("geometry");
                    JsonNode coordinates = geometry.path("coordinates");


                    double longitude = coordinates.get(0).asDouble();
                    double latitude = coordinates.get(1).asDouble();
                    double globina = coordinates.get(2).asDouble();

                    String geoLokacija = latitude + "," + longitude;

                    najhujsiPotres = new Potres(kraj, geoLokacija, globina);
                }
            }

            return najhujsiPotres;
        } catch (Exception e) {
            log.error("Napaka pri pridobitvi podatkov za zadnji teden", e);
            return new Potres("N/A", "N/A", 0.0);
        }
    }

    @Autowired
    private VremeService vremeService;

    @CircuitBreaker(name = "potresi", fallbackMethod = "fallbackZaPotrese")
    public Potres najdiZadnji() {
        String url = potresApiConfig.getZadnji();

        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode features = rootNode.path("features");


                JsonNode firstFeature = features.get(0);
                JsonNode properties = firstFeature.path("properties");
                JsonNode geometry = firstFeature.path("geometry");

                long time = properties.path("time").asLong();

                String kraj = properties.path("place").asText();
                JsonNode coordinates = geometry.path("coordinates");

                double longitude = coordinates.get(0).asDouble();
                double latitude = coordinates.get(1).asDouble();
                double globina = coordinates.get(2).asDouble();

                String geoLokacija = latitude + "," + longitude;
                Vreme vreme = vremeService.pridobiVreme(latitude, longitude,time);

                Potres zadnjiPotres = new Potres(kraj, geoLokacija, globina);
                zadnjiPotres.setVreme(vreme);
                return zadnjiPotres;

        } catch (Exception e) {
            log.error("Napaka pri pridobitvi podatkov za zadnji potres", e);
        }


        return new Potres("N/A", "N/A", 0.0);


    }

    private Potres fallbackZaPotrese(Exception e) {
        log.warn("Uporabljam fallback za potrese. Razlog: {}", e.getMessage());
        return new Potres("N/A (fallback)", "N/A,N/A", 0.0);
    }
}







