package com.example.car.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.car.config.ClientServiceProperties;
import com.example.car.entities.Client;
import com.example.car.services.ClientApi;

import reactor.core.publisher.Mono;

/**
 * Endpoint de diagnostic reprenant l etape de validation du TP.
 *
 * Il renvoie au choix le client enrichi ou le statut brut de l appel WebClient,
 * ce qui aide a distinguer un client absent d une panne de service.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ClientApi clientApi;
    private final ClientServiceProperties properties;
    private final WebClient.Builder builder;

    public TestController(ClientApi clientApi, ClientServiceProperties properties,
                          WebClient.Builder builder) {
        this.clientApi = clientApi;
        this.properties = properties;
        this.builder = builder;
    }

    @GetMapping("/client/{id}")
        public Mono<ResponseEntity<Client>> testClient(@PathVariable Long id) {
        return clientApi.findClientById(id)
                .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        String reponse = builder.build()
                .get()
                .uri("http://" + properties.getName() + properties.getPath())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("SERVICE-CLIENT injoignable")
                .block();
        return ResponseEntity.ok(reponse);
    }
}