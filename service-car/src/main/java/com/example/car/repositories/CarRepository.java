package com.example.car.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.car.entities.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByClientId(Long clientId);
}