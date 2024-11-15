package com.upao.govench.govench.config;




import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerAPIConfig {
    @Value("${Govench.openapi.dev-url}")
    private String devurl="http://localhost:8080/api/v1";
    @Value("${Govench.openapi.prod-url}")
    private String produrl = "https://govench-api.onrender.com";

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devurl);
        devServer.setDescription("OpenAPI Dev Server");

        Server prodServer = new Server();
        prodServer.setUrl(produrl);
        prodServer.setDescription("OpenAPI Prod Server");

        //Contacto
        Contact contact = new Contact();
        contact.setEmail("govench6@gmail.com");
        contact.setName("Govench");
        contact.setUrl("https://github.com/upao-govench");


        //Licencia
        License mitlicense = new License().name("MIT liscense").url("https://opensource.org/licenses/MIT");

        Info info = new Info().title("Api eventos tecnologicos").version("1.0").
                contact(contact).description("Api restfull para eventos tecnológicos")
                .termsOfService("https://github.com/upao-govench")
                .license(mitlicense);

        // Configuración de seguridad JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("JWT Authentication");

        Components components = new Components()
                .addSecuritySchemes("bearerAuth", securityScheme);

        // Requerimiento de seguridad para utilizar en las operaciones
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer,prodServer))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
