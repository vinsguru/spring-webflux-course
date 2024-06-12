package com.vinsguru.playground.tests.sec06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
public class CalculatorTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void calculator() {
        // success
        validate(20, 10, "+", 200, "30");
        validate(20, 10, "-", 200, "10");
        validate(20, 10, "*", 200, "200");
        validate(20, 10, "/", 200, "2");
        // bad requests
        validate(20, 0, "+", 400, "b cannot be 0");
        validate(20, 10, "@", 400, "operation header should be + - * /");
        validate(20, 10, null, 400, "operation header should be + - * /");
    }

    private void validate(int a, int b, String operation, int statusCode, String expectedResult) {
        this.client.get()
                   .uri("/calculator/{a}/{b}", a, b)
                   .headers(h -> {
                       if (Objects.nonNull(operation)) {
                           h.add("operation", operation);
                       }
                   })
                   .exchange()
                   .expectStatus().isEqualTo(statusCode)
                   .expectBody(String.class)
                   .value(s -> {
                       Assertions.assertNotNull(s);
                       Assertions.assertEquals(expectedResult, s);
                   });
    }

}
