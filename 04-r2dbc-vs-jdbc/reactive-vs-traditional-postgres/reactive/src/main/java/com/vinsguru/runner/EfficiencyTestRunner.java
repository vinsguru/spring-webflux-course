package com.vinsguru.runner;

import com.vinsguru.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@ConditionalOnProperty(value = "efficiency.test", havingValue = "true")
public class EfficiencyTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EfficiencyTestRunner.class);

    @Autowired
    private CustomerRepository repository;

    @Override
    public void run(String... args) {
        var atomicInteger = new AtomicInteger(0);
        log.info("starting");
        this.repository.findAll()
                       .doOnNext(c -> {
                           // for every customer, increment
                           // print for every million 
                           var count = atomicInteger.incrementAndGet();
                           if (count % 1_000_000 == 0) {
                               log.info("{}", count);
                           }
                       })
                       .then()
                       .block(); // for demo
        log.info("done");
    }

}
