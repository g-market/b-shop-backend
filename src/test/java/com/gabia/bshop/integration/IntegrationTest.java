package com.gabia.bshop.integration;

import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class IntegrationTest {

	private static final String REDIS_VERSION = "redis:6.2.7";
	private static final int REDIS_PORT = 6379;
	@ClassRule
	static GenericContainer<?> REDIS_CONTAINER;

	static {

		REDIS_CONTAINER = new GenericContainer<>(REDIS_VERSION)
			.withExposedPorts(REDIS_PORT);

		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	private static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.data.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(REDIS_PORT));
		// registry.add("spring.datasource.url", () -> "jdbc:tc:mysql:8.0.31://testDB");
	}
}
