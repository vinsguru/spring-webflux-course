package com.vinsguru.playground.tests.sec05;

import com.vinsguru.playground.sec05.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec05")
public class CustomerServiceTest {

    @Autowired
    private WebTestClient client;

    // just validate HTTP response status codes!
    // unauthorized - no token
    // unauthorized - invalid token
    // standard category - GET - success
    // standard category - POST/PUT/DELETE - forbidden
    // prime category - GET - success
    // prime category - POST/PUT/DELETE - success

    @Test
    public void unauthorized(){
        // no token
        this.client.get()
                   .uri("/customers")
                   .exchange()
                   .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
        // invalid token
        this.validateGet("secret", HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void standardCategory(){
        this.validateGet("secret123", HttpStatus.OK);
        this.validatePost("secret123", HttpStatus.FORBIDDEN);
    }

    @Test
    public void primeCategory(){
        this.validateGet("secret456", HttpStatus.OK);
        this.validatePost("secret456", HttpStatus.OK);
    }

    private void validateGet(String token, HttpStatus expectedStatus) {
        this.client.get()
                   .uri("/customers")
                   .header("auth-token", token)
                   .exchange()
                   .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus) {
        var dto = new CustomerDto(null, "marshal", "marshal@gmail.com");
        this.client.post()
                   .uri("/customers")
                   .bodyValue(dto)
                   .header("auth-token", token)
                   .exchange()
                   .expectStatus().isEqualTo(expectedStatus);
    }
}
