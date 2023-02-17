package com.gabia.bshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class MinioConfig {
	@Bean
	public MinioClient minioClient(
		@Value("${minio.endpoint}") final String endPoint,
		@Value("${minio.user}") final String accessKey,
		@Value("${minio.password}") final String secretKey) {
		return MinioClient.builder()
			.endpoint(endPoint)
			.credentials(accessKey, secretKey)
			.build();
	}
}
