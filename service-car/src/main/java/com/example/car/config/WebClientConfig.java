package com.example.car.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

/**
 * WebClient configure pour resoudre un nom de service Eureka.
 *
 * @LoadBalanced est indispensable : sans lui, WebClient tenterait de resoudre
 * "SERVICE-CLIENT" comme un nom d hote DNS et echouerait. C est l erreur la
 * plus frequente de ce TP.
 */
@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder(ClientServiceProperties properties) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getTimeoutMs())
                .responseTimeout(Duration.ofMillis(properties.getTimeoutMs()));

        return WebClient.builder()
                .baseUrl("http://" + properties.getName())
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}