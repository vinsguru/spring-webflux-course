package com.vinsguru.webfluxdemo.webtestclient;

import com.vinsguru.webfluxdemo.controller.ReactiveMathValidationController;
import com.vinsguru.webfluxdemo.dto.MultiplyRequestDto;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(ReactiveMathValidationController.class)
public class Lec04ErrorHandlingTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    public void errorHandlingTest(){
        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(1)));

        this.client
               .get()
                .uri("/reactive-math/square/{number}/throw", 5) // 10-20
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("allowed range is 10 - 20")
                .jsonPath("$.errorCode").isEqualTo(100)
                .jsonPath("$.input").isEqualTo(5);
    }

}
