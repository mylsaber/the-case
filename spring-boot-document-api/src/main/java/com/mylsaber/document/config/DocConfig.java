package com.mylsaber.document.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangdi
 */
@Configuration
public class DocConfig {
    private static final String TOKEN_NAME = "token";
    private static final String BASE_PACKAGE = "com.mylsaber.document";

    @Bean
    public OpenAPI customOpenApi() {
        Components components = new Components();
        components.addSecuritySchemes(
                TOKEN_NAME,
                new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .scheme("basic")
                        .name(TOKEN_NAME)
                        .in(SecurityScheme.In.HEADER)
                        .description("请求头")
        );

        Info info = new Info();
        info.title("Spring Boot Document API")
                .description("Spring Boot Document API")
                .version("1.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI()
                .components(components)
                .info(info);
    }

    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder()
                .group("users")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList(TOKEN_NAME));
                    return operation;
                })
                .packagesToScan(BASE_PACKAGE)
                .build();
    }
}
