package com.schdlr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@EnableScheduling
public class SchdlrApplication {

     @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Schdlr API")
                        .version("1.0.0")
                        .description("API documentation for the Schdlr application")
                        .contact(
                            new Contact()
                            .name("Erdi")
                            .email("erdisyla6@gmail.com"))
                        .license(
                        new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org")));
    }

    public static void main(String[] args) {
        SpringApplication.run(SchdlrApplication.class, args);
    }
}
