package com.gabia.bshop.integration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.service.ImageService;
import com.gabia.bshop.service.ItemImageService;
import com.gabia.bshop.util.ImageValidator;

import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageTest extends IntegrationTest {
	@Autowired
	private MinioClient minioClient;

	@Autowired
	private ItemImageRepository itemImageRepository;

	@Autowired
	private ItemImageService itemImageService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ImageValidator imageValidator;

	@Value("${minio.endpoint}")
	private String ENDPOINT;

	@BeforeAll
	void before() throws Exception{
		final String bucket = "images";

		final String fileName = "No_Image.jpg";
		minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());

		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "test images".getBytes());
		PutObjectArgs uploadObject = PutObjectArgs.builder()
			.bucket(bucket)
			.object(fileName)
			.stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
			.contentType(multipartFile.getContentType())
			.build();

		minioClient.putObject(uploadObject);

	}

	@DisplayName("정상적인 이미지 경로인지 확인한다")
	@Test
	void validatorTest(){
		// given
		final String NoImageUrl = ENDPOINT + "/images/No_Image.jpg";

		// when
		boolean isOk = imageValidator.validate(NoImageUrl);

		// then
		Assertions.assertEquals(true, isOk);
	}

	@DisplayName("이미지를 업로드 한다")
	@Test
	void imageUploadTest() throws Exception{
		// given
		final String bucket = "images";
		final String fileName = "test_image.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "test_image".getBytes());

		// when
		List<ImageResponse> imageResponseList = imageService.uploadImage(List.of(multipartFile).toArray(new MultipartFile[0]));

		final String url = imageResponseList.get(0).url();
		final String uploadFileName = url.substring(url.lastIndexOf("/") + 1);
		final GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(uploadFileName).build();

		final String actual = minioClient.getObject(getObjectArgs).object();

		// then
		Assertions.assertEquals(uploadFileName, actual);
	}

	@DisplayName("이미지를 상품에 등록한다.")
	@Test
	void addImageToItem(){

	}
	//
	// @DisplayName()
}