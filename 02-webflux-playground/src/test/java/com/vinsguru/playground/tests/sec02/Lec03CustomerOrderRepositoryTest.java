package com.vinsguru.playground.tests.sec02;

import com.vinsguru.playground.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec03CustomerOrderRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository repository;

    @Test
    public void productsOrderedByCustomer() {
       this.repository.getProductsOrderedByCustomer("mike")
                      .doOnNext(p -> log.info("{}", p))
                      .as(StepVerifier::create)
                      .expectNextCount(2)
                      .expectComplete()
                      .verify();
    }

    @Test
    public void orderDetailsByProduct() {
        this.repository.getOrderDetailsByProduct("iphone 20")
                       .doOnNext(dto -> log.info("{}", dto))
                       .as(StepVerifier::create)
                       .assertNext(dto -> Assertions.assertEquals(975, dto.amount()))
                       .assertNext(dto -> Assertions.assertEquals(950, dto.amount()))
                       .expectComplete()
                       .verify();
    }

}
