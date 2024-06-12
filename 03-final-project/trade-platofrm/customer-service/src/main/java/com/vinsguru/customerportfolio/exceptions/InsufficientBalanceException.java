package com.vinsguru.customerportfolio.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    private static final String MESSAGE = "Customer [id=%d] does not have enough funds to complete the transaction";

    public InsufficientBalanceException(Integer customerId){
        super(MESSAGE.formatted(customerId));
    }

}
