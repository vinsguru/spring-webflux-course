package com.vinsguru.aggregator.exceptions;

public class InvalidTradeRequestException extends RuntimeException {

    public InvalidTradeRequestException(String message){
        super(message);
    }

}
