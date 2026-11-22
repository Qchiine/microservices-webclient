package com.example.car;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.car.entities.Car;
import com.example.car.entities.Client;
import com.example.car.repositories.CarRepository;
import com.example.car.services.ClientApi;
import com.example.car.web.CarController;

import reactor.core.publisher.Mono;

/**
 * Le client distant est mocke : ces tests verifient l agregation et la
 * degradation quand le service client ne renvoie rien.
 */
@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
class CarControllerTests {

    @Autowired
    private CarController controller;

    @Autowired
    private CarRepository repository;

    @MockitoBean
    private ClientApi clientApi;

    @BeforeEach
    void vider() {
        repository.deleteAll();
    }

    private Car creer(String marque, long clientId) {
        return repository.save(new Car(null, marque, "Yaris", clientId));
    }

    @Test
    void findAll_agregeLeClientDistant() {
        creer("Toyota", 1L);
        when(clientApi.findClientById(anyLong()))
                .thenReturn(Mono.just(new Client(1L, "Salma", 22f)));

        List<Car> cars = controller.findAll();

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).getClient()).isNotNull();
        assertThat(cars.get(0).getClient().getNom()).isEqualTo("Salma");
    }

    @Test
    void findAll_resteUtilisableSiLeClientEstAbsent() {
        creer("Toyota", 1L);
        when(clientApi.findClientById(anyLong())).thenReturn(Mono.empty());

        List<Car> cars = controller.findAll();

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).getClient()).isNull();
    }

    @Test
    void findById_renvoie404SiVoitureAbsente() {
        assertThat(controller.findById(999L).getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void findByClient_filtreSurLIdentifiantClient() {
        creer("Toyota", 1L);
        creer("Dacia", 2L);
        when(clientApi.findClientById(anyLong()))
                .thenReturn(Mono.just(new Client(1L, "Salma", 22f)));

        List<Car> cars = controller.findByClient(1L);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).getMarque()).isEqualTo("Toyota");
    }

    @Test
    void save_persisteLaVoitureSansClient() {
        var reponse = controller.save(new Car(null, "Toyota", "Yaris", 1L));

        assertThat(reponse.getStatusCode().value()).isEqualTo(201);
        assertThat(reponse.getBody().getClient()).isNull();
        assertThat(repository.count()).isEqualTo(1);
    }
}