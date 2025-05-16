package si.telekom.potres.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.telekom.potres.configuration.VremeApiProperties;
import si.telekom.potres.model.Vreme;

@Slf4j

@Service
public class VremeService {
    private final RestTemplate restTemplate;
    private final VremeApiProperties vremeProps;

    @Autowired
    public VremeService(RestTemplate restTemplate, VremeApiProperties vremeProps) {
        this.restTemplate = restTemplate;
        this.vremeProps = vremeProps;
    }

    @CircuitBreaker(name = "vreme", fallbackMethod = "fallbackZaVreme")
    public Vreme pridobiVreme(double lat, double lng, long time) {
        long timeend;
        String url = String.format("%s?lat=%f&lon=%f&appid=%s&start=%s&end=%s&units=%s",
                vremeProps.getApiUrl(),
                lat,
                lng,
                vremeProps.getApiKey(),
                time,
                timeend=time++,
                vremeProps.getUnits());

        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            JsonNode main = rootNode.path("main");
            double temp = main.path("temp").asDouble();

            JsonNode vremeArray = rootNode.path("weather");
            if (vremeArray.size() > 0) {
                JsonNode prviWeather = vremeArray.get(0);
                String stanje = prviWeather.path("description").asText();
                return new Vreme(stanje, temp);
            }
        } catch (Exception e) {
            log.error("Napaka pri pridobivanju vremena: {}", e.getMessage());
        }

        return fallbackZaVreme(lat, lng, null);
    }

    private Vreme fallbackZaVreme(double lat, double lng, Exception e) {
        log.warn("Uporabljam fallback za vreme na lokaciji {}/{}. Razlog: {}", lat, lng, e.getMessage());
        return new Vreme("N/A", 0.0);
    }
}

