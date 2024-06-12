package com.vinsguru.aggregator.advice;

import com.vinsguru.aggregator.exceptions.CustomerNotFoundException;
import com.vinsguru.aggregator.exceptions.InvalidTradeRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/customer-not-found"));
            problem.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InvalidTradeRequestException.class)
    public ProblemDetail handleException(InvalidTradeRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/invalid-trade-request"));
            problem.setTitle("Invalid Trade Request");
        });
    }

    private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }

}
