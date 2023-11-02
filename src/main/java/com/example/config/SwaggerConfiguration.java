package com.example.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder().group("Api").pathsToMatch("/api/**").build();
    }

    @Bean
    public OpenAPI apiInfo() {
        final String securitySchemeName = "BearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .info(
                        new Info()
                                .title("JWT Authentication")
                                .description("Rest Api Secured with JWT(authentication) application")
                                .version("1.0"));
    }
    @Bean
    GroupedOpenApi authApiGroup() {
        return GroupedOpenApi.builder().group("authentication").pathsToMatch("/**/auth/**").build();
    }
    @Bean
    GroupedOpenApi customerApiGroup() {
        return GroupedOpenApi.builder().group("customer").pathsToMatch("/**/customer/**").build();
    }

    @Bean
    GroupedOpenApi productApiGroup() {
        return GroupedOpenApi.builder().group("product").pathsToMatch("/**/product/**").build();
    }

    @Bean
    GroupedOpenApi orderApiGroup() {
        return GroupedOpenApi.builder().group("order").pathsToMatch("/**/order/**").build();
    }

    @Bean
    GroupedOpenApi orderDetailApiGroup() {
        return GroupedOpenApi.builder().group("orderDetails").pathsToMatch("/**/orderDetails/**").build();
    }
}