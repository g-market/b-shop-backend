package com.gabia.bshop.util;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gabia.bshop.exception.InternalServerException;
import com.gabia.bshop.exception.NotFoundException;

import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageValidate {
	private final MinioClient minioClient;
	@Value("${minio.bucket}")
	private String bucketName;

	public boolean validate(final String imageUrl) {
		final String[] schemes = {"http", "https"};
		final UrlValidator urlValidator = new UrlValidator(schemes);

		if (!urlValidator.isValid(imageUrl)) {
			throw new NotFoundException(INCORRECT_URL_EXCEPTION);
		}

		final String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
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
