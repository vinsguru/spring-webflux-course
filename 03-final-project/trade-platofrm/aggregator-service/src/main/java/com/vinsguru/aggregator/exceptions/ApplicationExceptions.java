package com.vinsguru.aggregator.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer customerId){
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T> Mono<T> invalidTradeRequest(String message){
        return Mono.error(new InvalidTradeRequestException(message));
    }

    public static <T> Mono<T> missingTicker(){
        return Mono.error(new InvalidTradeRequestException("Ticker is required"));
    }

    public static <T> Mono<T> missingTradeAction(){
        return Mono.error(new InvalidTradeRequestException("Trade action is required"));
    }

    public static <T> Mono<T> invalidQuantity(){
        return Mono.error(new InvalidTradeRequestException("Quantity should be > 0"));
    }

}
