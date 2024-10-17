package com.upao.govench.govench.config;




import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerAPIConfig {
    @Value("${Govench.openapi.dev-url}/swagger-ui")
    private String devurl="http://localhost:8080/api/v1/swagger-ui";

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devurl);
        devServer.setDescription("OpenAPI Dev Server");

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

        return new OpenAPI().info(info).addServersItem(devServer);
    }
}
