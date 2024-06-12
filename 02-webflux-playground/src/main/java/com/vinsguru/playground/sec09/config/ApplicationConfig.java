package com.vinsguru.playground.sec09.config;

import com.vinsguru.playground.sec09.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {

    @Bean
    public Sinks.Many<ProductDto> sink(){
        return Sinks.many().replay().limit(1);
    }

}
