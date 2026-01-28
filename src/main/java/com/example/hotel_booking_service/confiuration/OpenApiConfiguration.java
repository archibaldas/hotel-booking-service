package com.example.hotel_booking_service.confiuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApiDescription(){
        Server localHostServer = new Server();
        localHostServer.setUrl("http://localhost:8080");
        localHostServer.setDescription("local env");

        Server productionServer = new Server();
        productionServer.setUrl("https://hotel_booking_service.example.com");
        productionServer.setDescription("production env");

        Contact contact = new Contact();
        contact.setName("Nikolay Arkhipov");
        contact.setEmail("archibaldas@yandex.ru");
        contact.setUrl("https://archibaldas.servehttp.com");

        License mitLicense = new License().name("GNU AGPLv3")
                .url("https://www.gnu.org/licenses/agpl-3.0.html");

        Info info = new Info()
                .title("Hotel Booking Service API")
                .version("1.0.0")
                .description("API description")
                .termsOfService("https://example.com/terms")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localHostServer, productionServer));

    }
}
