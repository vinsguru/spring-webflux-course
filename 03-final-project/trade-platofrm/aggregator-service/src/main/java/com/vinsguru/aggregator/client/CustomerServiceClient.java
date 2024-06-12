package com.vinsguru.aggregator.client;

import com.vinsguru.aggregator.dto.CustomerInformation;
import com.vinsguru.aggregator.dto.StockTradeRequest;
import com.vinsguru.aggregator.dto.StockTradeResponse;
import com.vinsguru.aggregator.exceptions.ApplicationExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class CustomerServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceClient.class);
    private final WebClient client;

    public CustomerServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.client.get()
                          .uri("/customers/{customerId}", customerId)
                          .retrieve()
                          .bodyToMono(CustomerInformation.class)
                          .onErrorResume(NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId));
    }

    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
        return this.client.post()
                          .uri("/customers/{customerId}/trade", customerId)
                          .bodyValue(request)
                          .retrieve()
                          .bodyToMono(StockTradeResponse.class)
                          .onErrorResume(NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId))
                          .onErrorResume(BadRequest.class, this::handleException);
    }

    private <T> Mono<T> handleException(BadRequest exception){
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        log.error("customer service problem detail: {}", pd);
        return ApplicationExceptions.invalidTradeRequest(message);
    }

}
