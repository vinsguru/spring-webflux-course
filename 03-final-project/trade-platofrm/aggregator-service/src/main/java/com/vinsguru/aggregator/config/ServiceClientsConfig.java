package com.vinsguru.aggregator.config;

import com.vinsguru.aggregator.client.CustomerServiceClient;
import com.vinsguru.aggregator.client.StockServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceClientsConfig {

    private static final Logger log = LoggerFactory.getLogger(ServiceClientsConfig.class);

    @Bean
    public CustomerServiceClient customerServiceClient(@Value("${customer.service.url}") String baseUrl) {
        return new CustomerServiceClient(createWebClient(baseUrl));
    }

    @Bean
    public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseUrl) {
        return new StockServiceClient(createWebClient(baseUrl));
    }

    private WebClient createWebClient(String baseUrl) {
        log.info("base url: {}", baseUrl);
        return WebClient.builder()
                        .baseUrl(baseUrl)
                        .build();
    }

}
