package com.example.academia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

   @Bean
   public OpenAPI customOpenAPI() {
      final String securitySchemeName = "bearerAuth";

      return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                  .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                              .name(securitySchemeName)
                              .type(SecurityScheme.Type.HTTP)
                              .scheme("bearer")
                              .bearerFormat("JWT")))
            .info(new Info()
                  .title("Academia Management API")
                  .description("API REST para gestión de academia con roles, matriculación y calendarios.")
                  .version("1.0.0")
                  .contact(new Contact()
                        .email("blasandtic@gmail.com")
                        .name("Blas"))
                  .license(new License()
                        .name("MIT")
                        .url("https://opensource.org/licenses/MIT")));
   }

}
