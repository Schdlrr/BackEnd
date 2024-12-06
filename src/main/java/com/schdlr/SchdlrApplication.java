package com.schdlr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
 /*
 * SchdlrApplication handles booting up the application and creating an
 * OpenAPI documentation for the application.
 * 
 * Responsibilities:
 * - Creating the OpenAPI instance for documentation.
 * - Booting up the application.
 * 
 * Annotations:
 * - @SpringBootApplication: Marks this class as a SpringBootApplication that can be run.
 * - @EnableScheduling: Allows for activities to be scheduled in the application.
 */
@SpringBootApplication
@EnableScheduling
public class SchdlrApplication {

    /*
     * Configures the OpenAPI documentation for the application.
     * 
     * returns an OpenAPI instance containing metadata about the Schdlr API.
     */
     @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Schdlr API") // The title of the API.
                        .version("1.0.0") // The version of the API.
                        .description("API documentation for the Schdlr application") // A brief description of the API.
                        .contact( // Contact information for the API's maintainer.
                            new Contact()
                            .name("Erdi") // Name of the contact person.
                            .email("erdisyla6@gmail.com")) // Email of the contact person.
                        .license(
                        new License() 
                        .name("Apache 2.0") // Name of the license.
                        .url("http://springdoc.org"))); // URL of the license details.
    }

    /*
     * The main method to run the Spring Boot application.
     * 
     * args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SchdlrApplication.class, args); // Boots up the application.
}
}
