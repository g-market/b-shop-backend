package com.gabia.bshop.integration.service;

import static com.gabia.bshop.exception.ErrorCode.*;
import static com.gabia.bshop.fixture.ItemFixture.*;

import java.util.List;

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

import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.request.ItemImageCreateRequest;
import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.service.ImageService;
import com.gabia.bshop.service.ItemImageService;
import com.gabia.bshop.util.ImageValidator;

import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageServiceTest extends IntegrationTest {
	@Autowired
	private MinioClient minioClient;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ItemImageService itemImageService;
	@Autowired
	private ImageService imageService;

	@Autowired
	private ItemImageRepository itemImageRepository;
	@Autowired
	private ImageValidator imageValidator;

	@Value("${minio.endpoint}")
	private String ENDPOINT;
	@Value("${minio.bucket}")
	private String BUCKET;

	@BeforeAll
	void before() throws Exception {

		final String fileName = "No_Image.jpg";
		minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET).build());

		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "test images".getBytes());
		PutObjectArgs uploadObject = PutObjectArgs.builder()
			.bucket(BUCKET)
			.object(fileName)
			.stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
			.contentType(multipartFile.getContentType())
			.build();

		minioClient.putObject(uploadObject);

	}

	@DisplayName("정상적인 이미지 경로인지 확인한다")
	@Test
	void validatorTest() {
		// given
		final String NoImageUrl = ENDPOINT + "/images/No_Image.jpg";

		// when
		boolean isOk = imageValidator.validate(NoImageUrl);

		// then
		Assertions.assertTrue(isOk);
	}

	@DisplayName("이미지를 업로드 한다")
	@Test
	void imageUploadTest() throws Exception {
		// given
		final String bucket = "images";
		final String fileName = "test_image.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "test_image".getBytes());

		// when
		final List<ImageResponse> imageResponseList = imageService.uploadImage(
			List.of(multipartFile).toArray(new MultipartFile[0]));

		final String url = imageResponseList.get(0).url();
		final String uploadFileName = url.substring(url.lastIndexOf("/") + 1);
		final GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(uploadFileName).build();

		final String actual = minioClient.getObject(getObjectArgs).object();

		// then
		Assertions.assertEquals(uploadFileName, actual);
	}

	@DisplayName("이미지를 상품에 등록한다")
	@Test
	void addImageToItem() {
		// given
		final String fileName = "create.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "create image".getBytes());
		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});

		final Category category1 = Category.builder()
			.name("카테고리1")
			.build();

		categoryRepository.save(category1);

		final Item item = itemRepository.save(ITEM_1.getInstance(category1));

		final List<String> urlList = List.of(imageResponseList.get(0).url());
		final ItemImageCreateRequest itemImageCreateRequest = new ItemImageCreateRequest(urlList);
		// when
		final List<ItemImageDto> expected = itemImageService.createItemImage(item.getId(), itemImageCreateRequest);
		final List<ItemImageDto> actual = itemImageService.findItemImageList(item.getId());
		// then
		Assertions.assertEquals(actual.get(0).url(), expected.get(0).url());
	}

	@DisplayName("상품의 이미지를 업데이트한다")
	@Test
	void updateItemImage() {
		// given
		final String fileName = "create.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "create image".getBytes());
		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});

		final Category category1 = Category.builder()
			.name("카테고리1")
			.build();

		categoryRepository.save(category1);

		final Item item = itemRepository.save(ITEM_1.getInstance(category1));
		final String url = imageResponseList.get(0).url();

		final ItemImage itemImage = ItemImage.builder().item(item).url(url).build();
		itemImageRepository.save(itemImage);

		// when
		final String expected =
			ENDPOINT + "/" + BUCKET + "/" + "No_Image.jpg"; // http://localhost:{port}/images/No_Image.jpg

		final ItemImageDto itemImageDto = new ItemImageDto(itemImage.getId(), expected);
		itemImageService.updateItemImage(item.getId(), itemImageDto);

		final ItemImage updatedItemImage = itemImageRepository.findByIdAndItemId(itemImageDto.imageId(), item.getId()).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemImageDto.imageId())
		);

		final String actual = updatedItemImage.getUrl();

		// then
		Assertions.assertEquals(expected, actual);
	}

	@DisplayName("상품의 이미지를 제거한다")
	@Test
	void deleteItemImage() {
		// given
		final String fileName = "create.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "create image".getBytes());
		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});

		final Category category1 = Category.builder()
			.name("카테고리1")
			.build();

		categoryRepository.save(category1);

		final Item item = itemRepository.save(ITEM_1.getInstance(category1));
		final String url = imageResponseList.get(0).url();

		final ItemImage itemImage = ItemImage.builder().item(item).url(url).build();
		itemImageRepository.save(itemImage);

		// when
		itemImageService.deleteItemImage(item.getId(), itemImage.getId());

		// then
		Assertions.assertThrows(
			NotFoundException.class,
			() -> {
				itemImageService.findItemImage(item.getId(), itemImage.getId());
			});
	}
}
