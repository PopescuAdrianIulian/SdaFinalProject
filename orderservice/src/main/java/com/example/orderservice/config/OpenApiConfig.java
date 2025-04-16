package com.example.orderservice.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryServiceApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("Order Service API for parcel tracking project using microservice architecture")
                        .version("v1.0.0")
                );
    }
}