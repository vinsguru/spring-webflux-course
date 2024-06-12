package com.vinsguru.playground.tests.sec07;

import com.vinsguru.playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec02FluxTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void streamingResponse() {
        this.client.get()
                   .uri("/lec02/product/stream")
                   .retrieve()
                   .bodyToFlux(Product.class)
                   .take(Duration.ofSeconds(3))
                   .doOnNext(print())
                   .then()
                   .as(StepVerifier::create)
                   .expectComplete()
                   .verify();
    }

}
