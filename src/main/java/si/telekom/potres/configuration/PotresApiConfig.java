package si.telekom.potres.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "potres.api.url")
@Getter
@Setter

// config za pridobiter URL-ja za potrese iz USGS
public class PotresApiConfig {
    private String base;
    private String teden;
    private String mesec;
    private String zadnji;


}


