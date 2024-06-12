package com.vinsguru.playground.tests.sec07;

import com.vinsguru.playground.tests.sec07.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec06QueryParamsTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void uriBuilderVariables() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        this.client.get()
                   .uri(builder -> builder.path(path).query(query).build(10, 20, "+"))
                   .retrieve()
                   .bodyToMono(CalculatorResponse.class)
                   .doOnNext(print())
                   .then()
                   .as(StepVerifier::create)
                   .expectComplete()
                   .verify();
    }

    @Test
    public void uriBuilderMap() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        var map = Map.of(
                "first", 10,
                "second", 20,
                "operation", "*"
        );
        this.client.get()
                   .uri(builder -> builder.path(path).query(query).build(map))
                   .retrieve()
                   .bodyToMono(CalculatorResponse.class)
                   .doOnNext(print())
                   .then()
                   .as(StepVerifier::create)
                   .expectComplete()
                   .verify();
    }

}
