package com.vinsguru.aggregator.controller;

import com.vinsguru.aggregator.dto.CustomerInformation;
import com.vinsguru.aggregator.dto.StockTradeResponse;
import com.vinsguru.aggregator.dto.TradeRequest;
import com.vinsguru.aggregator.service.CustomerPortfolioService;
import com.vinsguru.aggregator.validator.RequestValidator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return this.customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> mono) {
        return mono.transform(RequestValidator.validate())
                   .flatMap(req -> this.customerPortfolioService.trade(customerId, req));
    }

}
