package com.example.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.client.repositories.ClientRepository;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
@AutoConfigureMockMvc
class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository repository;

    @BeforeEach
    void vider() {
        repository.deleteAll();
    }

    @Test
    void post_creeUnClientEtRenvoie201() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Salma\",\"age\":22}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Salma"))
                .andExpect(jsonPath("$.id").isNumber());

        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void get_retourneTousLesClients() throws Exception {
        mockMvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\"Salma\",\"age\":22}"));
        mockMvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\"Ahmed\",\"age\":30}"));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_renvoie404SiAbsent() throws Exception {
        mockMvc.perform(get("/api/clients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_refuseUnNomVide() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"\",\"age\":22}"))
                .andExpect(status().isBadRequest());
    }
}