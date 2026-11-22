package com.example.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.example.car.config.ClientServiceProperties;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ClientServiceProperties.class)
public class ServiceCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCarApplication.class, args);
    }
}