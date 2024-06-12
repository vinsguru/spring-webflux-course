package com.vinsguru.aggregator.tests;


import org.junit.jupiter.api.BeforeAll;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@MockServerTest
@AutoConfigureWebTestClient
@SpringBootTest(properties = {
		"customer.service.url=http://localhost:${mockServerPort}",
		"stock.service.url=http://localhost:${mockServerPort}"
})
abstract class AbstractIntegrationTest {

	private static final Path TEST_RESOURCES_PATH = Path.of("src/test/resources");

	// it is set by @MockServerTest
	protected MockServerClient mockServerClient;

	@Autowired
	protected WebTestClient client;

	@BeforeAll
	public static void setup(){
		ConfigurationProperties.disableLogging(true);
	}

	protected String resourceToString(String relativePath){
        try {
            return Files.readString(TEST_RESOURCES_PATH.resolve(relativePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
