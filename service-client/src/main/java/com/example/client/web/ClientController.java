package com.example.client.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.entities.Client;
import com.example.client.repositories.ClientRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository repo;

    public ClientController(ClientRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Client> findAll() {
        return repo.findAll();
    }

    /**
     * Un identifiant inconnu renvoie 404 : le service voiture doit pouvoir
     * distinguer un client absent d une panne, sans recevoir un corps null.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Client> save(@RequestBody @Valid Client client) {
        client.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(client));
    }
}