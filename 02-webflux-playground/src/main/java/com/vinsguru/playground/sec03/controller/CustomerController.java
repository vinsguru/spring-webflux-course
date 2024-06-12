package com.vinsguru.playground.sec03.controller;

import com.vinsguru.playground.sec03.dto.CustomerDto;
import com.vinsguru.playground.sec03.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<CustomerDto>> getCustomer(@PathVariable Integer id) {
        return this.customerService.getCustomerById(id)
                                   .map(ResponseEntity::ok)
                                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return this.customerService.saveCustomer(mono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
        return this.customerService.updateCustomer(id, mono)
                                   .map(ResponseEntity::ok)
                                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer id) {
        return this.customerService.deleteCustomerById(id)
                                   .filter(b -> b)
                                   .map(b -> ResponseEntity.ok().<Void>build())
                                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
