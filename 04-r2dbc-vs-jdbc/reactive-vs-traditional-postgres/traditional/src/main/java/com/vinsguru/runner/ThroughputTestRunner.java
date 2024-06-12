package com.vinsguru.runner;

import com.vinsguru.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@ConditionalOnProperty(value = "throughput.test", havingValue = "true")
public class ThroughputTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ThroughputTestRunner.class);
    private static final int TASKS_COUNT = 100_000;

    @Autowired
    private CustomerRepository repository;

    @Value("${useVirtualThreadExecutor:false}")
    private boolean useVirtualThreadExecutor;

    @Override
    public void run(String... args)  {
        log.info("starting");
        log.info("is virtual thread executor?: {}", useVirtualThreadExecutor);
        for (int iteration = 1; iteration <= 10; iteration++) {
            // repeat the test 10 times. first run is for warm up
            var executor = useVirtualThreadExecutor ? getVirtualExecutor() : getFixedExecutor();
            measureTimeTaken(iteration, () -> runTest(executor));
        }
    }

    /*
        Make TASKS_COUNT calls.
        Each call is for fetching 1 single customer information
    */
    private void runTest(ExecutorService executorService){
        try(var executor = executorService){
            for (int i = 1; i <= TASKS_COUNT ; i++) {
                final var customerId = i;
                executor.submit(() -> this.repository.findById(customerId));
            }
        } // we wait for all the calls to complete
    }

    private void measureTimeTaken(int iteration, Runnable runnable){
        var start = System.currentTimeMillis();
        runnable.run();
        var timeTaken = (System.currentTimeMillis() - start); // in millis
        var throughput = (1.0 * TASKS_COUNT / timeTaken) * 1000; //  we multiply by 1000 to get throughput in seconds.
        log.info("test: {} - took: {} ms, throughput: {} / sec", iteration , timeTaken, throughput);
    }

    private ExecutorService getFixedExecutor(){
        return Executors.newFixedThreadPool(256); // reactor sends 256 at a time via flatMap
    }

    private ExecutorService getVirtualExecutor(){
        // This executor does not have any internal queue. All the submitted tasks will be executed concurrently!
        // You might want to use semaphore to limit the concurrency if it is important!
        return Executors.newVirtualThreadPerTaskExecutor();
    }

}
