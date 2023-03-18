package com.gabia.bshop.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.bshop.integration.IntegrationTest;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

@SpringBootTest
class MinioConfigTest extends IntegrationTest {
	@Autowired
	private MinioClient minioClient;

	@DisplayName("Minio 연결 후 테스트 버킷을 생성한다")
	@Test
	void configTest() throws Exception {
		// given
		final String bucket = "test-bucket";
		minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());

		// when
		final boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());

		// then
		Assertions.assertTrue(isExist);
	}
}
