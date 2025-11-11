package is.hi.hbv501gteam23.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Soccer API")
                    .version("1.0")
                    .description("API for managing soccer data including players, teams, matches, and venues")
                        .contact(new Contact()
                            .name("Team 23 - HBV501G")
                            .url("https://github.com/Thorsteinn-H/HBV501G-TEAM23")))
                .tags(List.of(
                    new Tag().name("Auth").description("Authentication and user management"),
                    new Tag().name("Venue").description("Venue management"),
                    new Tag().name("Team").description("Team management"),
                    new Tag().name("Player").description("Player management"),
                    new Tag().name("Match").description("Match management"),
                    new Tag().name("Favorite").description("Favorite management"),
                    new Tag().name("Metadata").description("Metadata endpoints")
                ))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                    .addSecuritySchemes("BearerAuth",
                        new SecurityScheme()
                            .name("Authorization")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
                );
    }
}
