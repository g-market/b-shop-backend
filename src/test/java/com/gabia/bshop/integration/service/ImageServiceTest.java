package com.gabia.bshop.integration.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.service.ImageService;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImageServiceTest extends IntegrationTest {
	@Autowired
	private MinioClient minioClient;
	@Autowired
	private ImageService imageService;
	@Value("${minio.bucket}")
	private String bucket;

	@BeforeAll
	void before() throws Exception {
		if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
		}
	}

	@DisplayName("이미지를 업로드 한다")
	@Test
	void imageUploadTest() throws Exception {
		// given
		final String fileName = "test_image.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "test_image".getBytes());

		// when
		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});

		final String url = imageResponseList.get(0).url();
		final String uploadFileName = url.substring(url.lastIndexOf("/") + 1);
		final GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(uploadFileName).build();

		final String actual = minioClient.getObject(getObjectArgs).object();
		// then
		Assertions.assertEquals(uploadFileName, actual);
	}

	@DisplayName("이미지를 업로드 최대 갯수 보다 많으면 예외를 터트린다")
	@Test
	void imageUploadWhenSizeLargerThanMaxSize() {
		// given
		final String fileName = "test_image.jpg";

		MultipartFile[] multipartFileList = IntStream.rangeClosed(1, 11)
			.mapToObj(index ->
				new MockMultipartFile(
					fileName + index, fileName + index, "image/jpg",
					"test_image".getBytes())
			)
			.toArray(MultipartFile[]::new);

		// when & then
		assertThatThrownBy(
			() -> imageService.uploadImage(multipartFileList)
		);
	}
}
