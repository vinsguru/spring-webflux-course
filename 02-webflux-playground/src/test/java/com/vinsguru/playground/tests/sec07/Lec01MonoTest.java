package com.vinsguru.playground.tests.sec07;

import com.vinsguru.playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class Lec01MonoTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void simpleGet() throws InterruptedException {
        this.client.get()
                   .uri("/lec01/product/1")
                   .retrieve()
                   .bodyToMono(Product.class)
                   .doOnNext(print())
                   .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    public void concurrentRequests() throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            this.client.get()
                       .uri("/lec01/product/{id}", i)
                       .retrieve()
                       .bodyToMono(Product.class)
                       .doOnNext(print())
                       .subscribe();
        }

        Thread.sleep(Duration.ofSeconds(2));
    }

}
