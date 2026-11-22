package com.example.car;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Verifie le point sensible du TP : le filtre du load balancer est attache au
 * Builder annote, donc reutiliser un WebClient deja construit le perd.
 */
@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
class WebClientLoadBalancedTests {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private WebClient.Builder builderAnnnote;

    @Test
    void leFiltreDeLoadBalancerEstPresentDansLeContexte() {
        assertThat(context.getBeanNamesForType(DeferringLoadBalancerExchangeFilterFunction.class))
                .isNotEmpty();
    }

    @Test
    void leBuilderAnnnoteConstruitUnClient() {
        WebClient client = builderAnnnote.build();

        assertThat(client).isNotNull();
    }
}