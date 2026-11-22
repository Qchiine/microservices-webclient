package com.example.car.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import com.example.car.services.ClientApi;

import jakarta.validation.Valid;

/**
 * Endpoints REST des voitures, avec enrichissement par le service client.
 *
 * Le pattern reste volontairement simple : lecture locale puis appel HTTP.
 * block() est acceptable dans ce TP mais serait a remplacer par une chaine
 * entierement reactive en production.
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository repo;
    private final ClientApi clientApi;

    public CarController(CarRepository repo, ClientApi clientApi) {
        this.repo = repo;
        this.clientApi = clientApi;
    }

    @PostMapping
    public ResponseEntity<Car> save(@RequestBody @Valid Car car) {
        car.setId(null);
        car.setClient(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(car));
    }

    @GetMapping
    public List<Car> findAll() {
        List<Car> cars = repo.findAll();
        for (Car car : cars) {
            enrichir(car);
        }
        return cars;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id) {
        return repo.findById(id)
                .map(car -> {
                    enrichir(car);
                    return ResponseEntity.ok(car);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byClient/{clientId}")
    public List<Car> findByClient(@PathVariable Long clientId) {
        List<Car> cars = repo.findByClientId(clientId);
        for (Car car : cars) {
            enrichir(car);
        }
        return cars;
    }

    private void enrichir(Car car) {
        if (car.getClientId() != null) {
            car.setClient(clientApi.findClientById(car.getClientId()).block());
        }
    }
}