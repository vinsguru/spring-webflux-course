package com.vinsguru.playground.sec06.config;

import com.vinsguru.playground.sec06.dto.CustomerDto;
import com.vinsguru.playground.sec06.exceptions.ApplicationExceptions;
import com.vinsguru.playground.sec06.service.CustomerService;
import com.vinsguru.playground.sec06.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    @Autowired
    private CustomerService customerService;

    public Mono<ServerResponse> allCustomers(ServerRequest request) {
        //request.pathVariable()
        //request.headers()
        //request.queryParam()
        return this.customerService.getAllCustomers()
                                   .as(flux -> ServerResponse.ok().body(flux, CustomerDto.class));
    }

    public Mono<ServerResponse> paginatedCustomers(ServerRequest request) {
        var page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        var size = request.queryParam("size").map(Integer::parseInt).orElse(3);
        return this.customerService.getAllCustomers(page, size)
                                   .collectList()
                                   .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.getCustomerById(id)
                                   .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                                   .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDto.class)
                      .transform(RequestValidator.validate())
                      .as(this.customerService::saveCustomer)
                      .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(CustomerDto.class)
                      .transform(RequestValidator.validate())
                      .as(validatedReq -> this.customerService.updateCustomer(id, validatedReq))
                      .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                      .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.deleteCustomerById(id)
                                   .filter(b -> b)
                                   .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                                   .then(ServerResponse.ok().build());
    }

}
