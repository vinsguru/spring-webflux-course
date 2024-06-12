package com.vinsguru.playground.tests.sec02;

import com.vinsguru.playground.sec02.entity.Customer;
import com.vinsguru.playground.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec01CustomerRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository repository;

    @Test
    public void findAll() {
        this.repository.findAll()
                       .doOnNext(c -> log.info("{}", c))
                       .as(StepVerifier::create)
                       .expectNextCount(10)
                       .expectComplete()
                       .verify();
    }

    @Test
    public void findById() {
        this.repository.findById(2)
                       .doOnNext(c -> log.info("{}", c))
                       .as(StepVerifier::create)
                       .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                       .expectComplete()
                       .verify();
    }

    @Test
    public void findByName() {
        this.repository.findByName("jake")
                       .doOnNext(c -> log.info("{}", c))
                       .as(StepVerifier::create)
                       .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                       .expectComplete()
                       .verify();
    }

    /*
        Query methods
        https://docs.spring.io/spring-data/relational/reference/r2dbc/query-methods.html

        Task: find all customers whose email ending with "ke@gmail.com"
    */

    @Test
    public void findByEmailEndingWith() {
        this.repository.findByEmailEndingWith("ke@gmail.com")
                       .doOnNext(c -> log.info("{}", c))
                       .as(StepVerifier::create)
                       .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                       .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                       .expectComplete()
                       .verify();
    }

    @Test
    public void insertAndDeleteCustomer() {
        // insert
        var customer = new Customer();
        customer.setName("marshal");
        customer.setEmail("marshal@gmail.com");
        this.repository.save(customer)
                       .doOnNext(c -> log.info("{}", c))
                       .as(StepVerifier::create)
                       .assertNext(c -> Assertions.assertNotNull(c.getId()))
                       .expectComplete()
                       .verify();
        // count
        this.repository.count()
                       .as(StepVerifier::create)
                       .expectNext(11L)
                       .expectComplete()
                       .verify();
        // delete
        this.repository.deleteById(11)
                       .then(this.repository.count())
                       .as(StepVerifier::create)
                       .expectNext(10L)
                       .expectComplete()
                       .verify();
    }

    @Test
    public void updateCustomer() {
        this.repository.findByName("ethan")
                       .doOnNext(c -> c.setName("noel")) // It is for mutating!
                       .flatMap(c -> this.repository.save(c))
                       .doOnNext(c -> log.info("{}", c))
                       .as(StepVerifier::create)
                       .assertNext(c -> Assertions.assertEquals("noel", c.getName()))
                       .expectComplete()
                       .verify();
    }

}
