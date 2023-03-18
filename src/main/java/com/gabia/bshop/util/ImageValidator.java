package com.gabia.bshop.util;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gabia.bshop.exception.InternalServerException;

import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageValidator {
	private final MinioClient minioClient;
	@Value("${minio.bucket}")
	private String bucketName;

	public boolean validate(final String imageName) {
		try {
			minioClient.statObject(StatObjectArgs.builder()
				.bucket(bucketName)
				.object(imageName)
				.build());
			return true;
		} catch (ErrorResponseException e) {
			return false;
		} catch (Exception e) {
			throw new InternalServerException(MINIO_EXCEPTION);
		}
	}
}
