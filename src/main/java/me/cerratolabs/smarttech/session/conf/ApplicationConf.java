package me.cerratolabs.smarttech.session.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "session")
public class ApplicationConf {

    private String publicKeyPath = "/smarttech.pub";
    private String privateKeyPath = "/smarttech.pem";
    private Long jwtExpirationInMs = 1800000L;

}