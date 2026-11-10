package tn.esprit.autoloc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration de la documentation Swagger : http://localhost:8080/swagger-ui.html */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autoLocOpenApi() {
        return new OpenAPI().info(new Info()
                .title("AutoLoc API")
                .version("1.0")
                .description("API REST de la plateforme de location de vehicules multi-agences AutoLoc "
                        + "(UP ASI - Architecture des Systemes d'Information)")
                .contact(new Contact().name("Esprit - UP ASI")));
    }
}
