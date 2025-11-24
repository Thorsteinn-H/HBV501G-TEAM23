package is.hi.hbv501gteam23.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
            pathItem.readOperationsMap().forEach((httpMethod, op) -> {
                ApiResponses responses = op.getResponses();

                if (!responses.containsKey("200")) {
                    responses.addApiResponse("200", createResponse("Success"));
                }

                if (!responses.containsKey("201") && httpMethod == HttpMethod.POST) {
                    responses.addApiResponse("201", createResponse("Created"));
                }

                if (!responses.containsKey("204") && httpMethod == HttpMethod.DELETE) {
                    responses.addApiResponse("204", createResponse("Deleted"));
                }

                if (!responses.containsKey("400")) responses.addApiResponse("400", createResponse("Invalid request"));
                if (!responses.containsKey("401")) responses.addApiResponse("401", createResponse("Unauthorized"));
                if (!responses.containsKey("403")) responses.addApiResponse("403", createResponse("Forbidden"));
                if (!responses.containsKey("404")) responses.addApiResponse("404", createResponse("Not Found"));
                if (!responses.containsKey("500")) responses.addApiResponse("500", createResponse("Internal Server Error"));

                op.getResponses().values().forEach(resp -> {
                    if (resp.getContent() != null) resp.setContent(null);
                    if (resp.getLinks() != null) resp.setLinks(null);
                });
            })
        );
    }

    private ApiResponse createResponse(String description) {
        return new ApiResponse().description(description);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Soccer API")
                .version("1.0")
                .description("Available endpoints for the Soccer API"))
            .tags(List.of(
                new Tag().name("Auth"),
                new Tag().name("User"),
                new Tag().name("Profile"),
                new Tag().name("Venue"),
                new Tag().name("Team"),
                new Tag().name("Player"),
                new Tag().name("Match"),
                new Tag().name("Favorite"),
                new Tag().name("Metadata")
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
