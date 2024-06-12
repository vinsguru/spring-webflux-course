package com.vinsguru.playground.sec05.controller;

import com.vinsguru.playground.sec05.dto.CustomerDto;
import com.vinsguru.playground.sec05.exceptions.ApplicationExceptions;
import com.vinsguru.playground.sec05.service.CustomerService;
import com.vinsguru.playground.sec05.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> allCustomers() {
        return this.customerService.getAllCustomers();
    }

    @GetMapping("paginated")
    public Mono<List<CustomerDto>> allCustomers(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "3") Integer size) {
        return this.customerService.getAllCustomers(page, size)
                                   .collectList();
    }

    @GetMapping("{id}")
    public Mono<CustomerDto> getCustomer(@PathVariable Integer id) {
        return this.customerService.getCustomerById(id)
                                   .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                   .as(this.customerService::saveCustomer);
    }

    @PutMapping("{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                   .as(validReq -> this.customerService.updateCustomer(id, validReq))
                   .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {
        return this.customerService.deleteCustomerById(id)
                                   .filter(b -> b)
                                   .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                                   .then();
    }

}
