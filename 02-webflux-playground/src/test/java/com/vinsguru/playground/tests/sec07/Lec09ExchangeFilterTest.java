package com.vinsguru.playground.tests.sec07;

import com.vinsguru.playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec09ExchangeFilterTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(Lec09ExchangeFilterTest.class);
    private final WebClient client = createWebClient(b -> b.filter(tokenGenerator())
                                                           .filter(requestLogger()));

    @Test
    public void exchangeFilter() {
        for (int i = 1; i <= 5; i++) {
            this.client.get()
                       .uri("/lec09/product/{id}", i)
                       .attribute("enable-logging", i % 2 == 0)
                       .retrieve()
                       .bodyToMono(Product.class)
                       .doOnNext(print())
                       .then()
                       .as(StepVerifier::create)
                       .expectComplete()
                       .verify();
        }
    }

    private ExchangeFilterFunction tokenGenerator() {
        return (request, next) -> {
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("generated token: {}", token);
            var modifiedRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth(token)).build();
            return next.exchange(modifiedRequest);
        };
    }

    private ExchangeFilterFunction requestLogger() {
        return (request, next) -> {
            var isEnabled = (Boolean) request.attributes().getOrDefault("enable-logging", false);
            if(isEnabled){
                log.info("request url - {}: {}", request.method(), request.url());
            }
            return next.exchange(request);
        };
    }


}
