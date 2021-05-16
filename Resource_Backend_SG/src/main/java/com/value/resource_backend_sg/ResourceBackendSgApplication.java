package com.value.resource_backend_sg;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableMongoAuditing
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
public class ResourceBackendSgApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceBackendSgApplication.class, args);


    }

}
