package com.gabia.bshop.integration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public abstract class IntegrationTest {

	private static final String REDIS_VERSION = "redis:6.2.7";
	private static final int REDIS_PORT = 6379;

	private static final String MINIO_VERSION = "quay.io/minio/minio:latest";

	private static final int MINIO_PORT = 9000;
	private static final Map<String, String> MINIO_ENV = new HashMap<>();

	private static String MINIO_ROOT_USER = "minio";

	private static String MINIO_ROOT_PASSWORD = "minio1234";

	@ClassRule
	static GenericContainer<?> REDIS_CONTAINER;

	@ClassRule
	static GenericContainer<?> MINIO_CONTAINER;

	static {
		REDIS_CONTAINER = new GenericContainer<>(REDIS_VERSION)
			.withExposedPorts(REDIS_PORT);

		MINIO_ENV.put("MINIO_ROOT_USER", MINIO_ROOT_USER);
		MINIO_ENV.put("MINIO_ROOT_PASSWORD", MINIO_ROOT_PASSWORD);

		MINIO_CONTAINER = new GenericContainer<>(MINIO_VERSION)
			.withEnv("MINIO_ROOT_USER", MINIO_ROOT_USER)
			.withEnv("MINIO_ROOT_PASSWORD", MINIO_ROOT_PASSWORD)
			.withCommand("server /data --address :9000")
			.withExposedPorts(MINIO_PORT)
			.waitingFor(new HttpWaitStrategy()
				.forPath("/minio/health/ready")
				.forPort(MINIO_PORT)
				.withStartupTimeout(Duration.ofSeconds(10)));

		REDIS_CONTAINER.start();
		MINIO_CONTAINER.start();
	}
	private static String getContainerAddress(final GenericContainer<?> container, final int port){
		return "http://" + container.getHost() + ":" + container.getMappedPort(port);
	}
	@DynamicPropertySource
	private static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.data.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(REDIS_PORT));
		registry.add("minio.endpoint", () -> getContainerAddress(MINIO_CONTAINER, MINIO_PORT));
		registry.add("minio.user", () -> MINIO_ROOT_USER);
		registry.add("minio.password", () -> MINIO_ROOT_PASSWORD);
		registry.add("minio.bucket", () -> "images");
		registry.add("minio.default.image", () -> getContainerAddress(MINIO_CONTAINER, MINIO_PORT) + "/images/No_Image.jpg");
	}
}
