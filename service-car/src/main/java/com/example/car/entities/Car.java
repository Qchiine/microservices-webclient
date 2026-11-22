package com.example.car.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Voiture persistee.
 *
 * clientId est un lien logique : les deux services ont des bases separees,
 * donc aucune relation JPA ne peut exister entre elles.
 */
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La marque est obligatoire.")
    private String marque;

    @NotBlank(message = "Le modele est obligatoire.")
    private String modele;

    @NotNull(message = "L'identifiant du client est obligatoire.")
    @Column(name = "client_id")
    private Long clientId;

    @Transient
    private Client client;

    public Car() {
    }

    public Car(Long id, String marque, String modele, Long clientId) {
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}