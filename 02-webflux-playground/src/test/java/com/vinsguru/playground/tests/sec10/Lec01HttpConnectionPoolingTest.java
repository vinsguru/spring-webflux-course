package com.vinsguru.playground.tests.sec10;

import com.vinsguru.playground.tests.sec10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Lec01HttpConnectionPoolingTest extends AbstractWebClient {

    /*
        It is for demo purposes! You might NOT need to adjust all these!
        If the response time 100ms => 500 / (100 ms) ==> 5000 req / sec
     */
    private final WebClient client = createWebClient(b -> {
        var poolSize = 10000;
        var provider = ConnectionProvider.builder("vins")
                                         .lifo()
                                         .maxConnections(poolSize)
                                         .pendingAcquireMaxCount(poolSize * 5)
                                         .build();
        var httpClient = HttpClient.create(provider)
                                   .compress(true)
                                   .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() {
        var max = 10000;
        Flux.range(1, max)
            .flatMap(this::getProduct, max)
            .collectList()
            .as(StepVerifier::create)
            .assertNext(l -> Assertions.assertEquals(max, l.size()))
            .expectComplete()
            .verify();
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                          .uri("/product/{id}", id)
                          .retrieve()
                          .bodyToMono(Product.class);
    }
}
