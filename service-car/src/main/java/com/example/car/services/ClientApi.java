package com.example.car.services;

import java.time.Duration;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.car.config.ClientServiceProperties;
import com.example.car.entities.Client;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Appel du service client par son nom Eureka.
 *
 * WebClient est non bloquant par nature : la methode renvoie un Mono que
 * l appelant decide de souscrire. Le controller bloque explicitement car le TP
 * expose des endpoints REST classiques.
 */
@Service
@Slf4j
public class ClientApi {

    private final WebClient webClient;
    private final ClientServiceProperties properties;

    public ClientApi(WebClient.Builder loadBalancedWebClientBuilder,
                     ClientServiceProperties properties) {
        this.properties = properties;
        this.webClient = loadBalancedWebClientBuilder.build();
    }

    public Mono<Client> findClientById(Long id) {
        return webClient.get()
                .uri(properties.getPath() + "/" + id)
                .retrieve()
                .bodyToMono(Client.class)
                .timeout(Duration.ofMillis(properties.getTimeoutMs()))
                .onErrorResume(this::clientIndisponible);
    }

    /**
     * Un client absent (404) ou un service en panne ne doit pas casser la
     * lecture des voitures : le Mono se termine vide et l appelant enrichit
     * avec un client null.
     */
    private Mono<Client> clientIndisponible(Throwable erreur) {
        if (erreur instanceof WebClientResponseException.NotFound) {
            log.info("Client {} absent du referentiel", erreur.getMessage());
        } else {
            log.warn("Service client indisponible : {}", erreur.getMessage());
        }
        return Mono.empty();
    }
}