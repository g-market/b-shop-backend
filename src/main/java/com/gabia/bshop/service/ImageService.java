package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.InternalServerException;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageService {

	private final MinioClient minioClient;

	private final int MAX_IMAGE_UPLOAD_COUNT = 10; // TO_BE_CHANGED
	@Value("${minio.bucket}")
	private String bucketName;

	@Value("${minio.endpoint}")
	private String endpoint;

	public List<ImageResponse> uploadImage(final MultipartFile[] fileList) {
		
		if (fileList.length > MAX_IMAGE_UPLOAD_COUNT) {
			throw new ConflictException(MAX_FILE_UPLOAD_REQUEST_EXCEPTION, MAX_IMAGE_UPLOAD_COUNT);
		}
		List<ImageResponse> imageResponseList = new ArrayList<>();

		try {
			for (MultipartFile file : fileList) {
				final String fileName = UUID.randomUUID().toString();
				final PutObjectArgs putObjectArgs = PutObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.stream(file.getInputStream(), file.getSize(), -1)
					.contentType(file.getContentType())
					.build();

				minioClient.putObject(putObjectArgs);

				imageResponseList.add(ImageResponse.builder()
					.fileName(file.getOriginalFilename())
					.url(endpoint + "/" + bucketName + "/" + fileName).build()
				);
			}
		} catch (Exception e) {
			throw new InternalServerException(MINIO_UPLOAD_EXCEPTION);
		}

		return imageResponseList;

	}
}