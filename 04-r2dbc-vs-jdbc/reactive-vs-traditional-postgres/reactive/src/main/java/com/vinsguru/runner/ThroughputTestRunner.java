package com.vinsguru.runner;

import com.vinsguru.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@ConditionalOnProperty(value = "throughput.test", havingValue = "true")
public class ThroughputTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ThroughputTestRunner.class);
    private static final int TASKS_COUNT = 100_000;

    @Autowired
    private CustomerRepository repository;

    @Override
    public void run(String... args) throws Exception {
        log.info("starting");

        for (int iteration = 1; iteration <= 10; iteration++) {
            // repeat the test 10 times. first run is for warm up
            measureTimeTaken(iteration, this::runTest);
        }
    }

    /*
        Make TASKS_COUNT calls.
        Each call is for fetching 1 single customer information
     */
    private void runTest() {
        Flux.range(1, TASKS_COUNT)
            .flatMap(id -> this.repository.findById(id))
            .then()
            .block(); // wait for all the tasks to complete
    }

    private void measureTimeTaken(int iteration, Runnable runnable){
        var start = System.currentTimeMillis();
        runnable.run();
        var timeTaken = (System.currentTimeMillis() - start); // in millis
        var throughput = (1.0 * TASKS_COUNT / timeTaken) * 1000; //  we multiply by 1000 to get throughput in seconds.
        log.info("test: {} - took: {} ms, throughput: {} / sec", iteration , timeTaken, throughput);
    }

}
