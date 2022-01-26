package io.camunda.getstarted.potpourri.spring;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Application class for this Spring Boot application.  When this Spring Boot app starts, it will make available all
 * defined ZeebeWorkers.
 *
 * @author chris
 */
@SpringBootApplication
@EnableZeebeClient
@ZeebeDeployment(resources = "classpath*:*.bpmn" )
public class CCPotpourriApplication {

    Logger log = LoggerFactory.getLogger(CCPotpourriApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CCPotpourriApplication.class, args);
    }

    /**
     * Whatever code is in init() will get executed once the Spring Boot app has started.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // code here if needed
        log.info("...Spring Boot has completed startup...");
    }

}
