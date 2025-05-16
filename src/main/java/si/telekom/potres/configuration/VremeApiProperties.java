package si.telekom.potres.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "openweather")

// config za pridobitev URL-ja Openweather API
public class VremeApiProperties {

    private String apiUrl;
    private String apiKey;
    private String units = "metric";

}
