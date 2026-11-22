package com.example.car.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Parametres du service client distant.
 *
 * Nom logique, chemin et timeout sont externalises : changer d environnement
 * ne demande aucune modification de code.
 */
@ConfigurationProperties(prefix = "client.service")
public class ClientServiceProperties {

    private String name = "SERVICE-CLIENT";
    private String path = "/api/clients";
    private int timeoutMs = 5000;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
}