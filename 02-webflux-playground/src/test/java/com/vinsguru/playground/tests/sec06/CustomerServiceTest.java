package com.vinsguru.playground.tests.sec06;

import com.vinsguru.playground.sec04.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void allCustomers() {
        this.client.get()
                   .uri("/customers")
                   .exchange()
                   .expectStatus().is2xxSuccessful()
                   .expectHeader().contentType(MediaType.APPLICATION_JSON)
                   .expectBodyList(CustomerDto.class)
                   .value(list -> log.info("{}", list))
                   .hasSize(10);
    }

    @Test
    public void paginatedCustomers() {
        this.client.get()
                   .uri("/customers/paginated?page=3&size=2")
                   .exchange()
                   .expectStatus().is2xxSuccessful()
                   .expectBody()
                   .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                   .jsonPath("$.length()").isEqualTo(2)
                   .jsonPath("$[0].id").isEqualTo(5)
                   .jsonPath("$[1].id").isEqualTo(6);
    }

    @Test
    public void customerById() {
        this.client.get()
                   .uri("/customers/1")
                   .exchange()
                   .expectStatus().is2xxSuccessful()
                   .expectBody()
                   .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                   .jsonPath("$.id").isEqualTo(1)
                   .jsonPath("$.name").isEqualTo("sam")
                   .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        // create
        var dto = new CustomerDto(null, "marshal", "marshal@gmail.com");
        this.client.post()
                   .uri("/customers")
                   .bodyValue(dto)
                   .exchange()
                   .expectStatus().is2xxSuccessful()
                   .expectBody()
                   .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                   .jsonPath("$.id").isEqualTo(11)
                   .jsonPath("$.name").isEqualTo("marshal")
                   .jsonPath("$.email").isEqualTo("marshal@gmail.com");

        // delete
        this.client.delete()
                   .uri("/customers/11")
                   .exchange()
                   .expectStatus().is2xxSuccessful()
                   .expectBody().isEmpty();
    }

    @Test
    public void updateCustomer() {
        var dto = new CustomerDto(null, "noel", "noel@gmail.com");
        this.client.put()
                   .uri("/customers/10")
                   .bodyValue(dto)
                   .exchange()
                   .expectStatus().is2xxSuccessful()
                   .expectBody()
                   .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                   .jsonPath("$.id").isEqualTo(10)
                   .jsonPath("$.name").isEqualTo("noel")
                   .jsonPath("$.email").isEqualTo("noel@gmail.com");
    }

    @Test
    public void customerNotFound() {
        // get
        this.client.get()
                   .uri("/customers/11")
                   .exchange()
                   .expectStatus().is4xxClientError()
                   .expectBody()
                   .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found");

        // delete
        this.client.delete()
                   .uri("/customers/11")
                   .exchange()
                   .expectStatus().is4xxClientError()
                   .expectBody()
                   .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found");

        // put
        var dto = new CustomerDto(null, "noel", "noel@gmail.com");
        this.client.put()
                   .uri("/customers/11")
                   .bodyValue(dto)
                   .exchange()
                   .expectStatus().is4xxClientError()
                   .expectBody()
                   .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found");
    }

    @Test
    public void invalidInput(){
        // missing name
        var missingName = new CustomerDto(null, null, "noel@gmail.com");
        this.client.post()
                   .uri("/customers")
                   .bodyValue(missingName)
                   .exchange()
                   .expectStatus().is4xxClientError()
                   .expectBody()
                   .jsonPath("$.detail").isEqualTo("Name is required");

        // missing email
        var missingEmail = new CustomerDto(null, "noel", null);
        this.client.post()
                   .uri("/customers")
                   .bodyValue(missingEmail)
                   .exchange()
                   .expectStatus().is4xxClientError()
                   .expectBody()
                   .jsonPath("$.detail").isEqualTo("Valid email is required");

        // invalid email
        var invalidEmail = new CustomerDto(null, "noel", "noel");
        this.client.put()
                   .uri("/customers/10")
                   .bodyValue(invalidEmail)
                   .exchange()
                   .expectStatus().is4xxClientError()
                   .expectBody()
                   .jsonPath("$.detail").isEqualTo("Valid email is required");
    }

}
