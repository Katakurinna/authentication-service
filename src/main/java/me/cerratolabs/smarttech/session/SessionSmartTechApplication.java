package me.cerratolabs.smarttech.session;

import me.cerratolabs.smarttech.session.conf.ApplicationConf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(ApplicationConf.class)
public class SessionSmartTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionSmartTechApplication.class, args);
    }

}
