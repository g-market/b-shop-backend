package com.gabia.bshop.integration.service;

import static com.gabia.bshop.exception.ErrorCode.*;
import static com.gabia.bshop.fixture.ItemFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.bshop.dto.request.ItemImageCreateRequest;
import com.gabia.bshop.dto.request.ItemImageUpdateRequest;
import com.gabia.bshop.dto.request.ItemThumbnailUpdateRequest;
import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.dto.response.ItemImageResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.service.ImageService;
import com.gabia.bshop.service.ItemImageService;
import com.gabia.bshop.util.ImageValidator;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemImageServiceTest extends IntegrationTest {

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
	private ImageValidator imageValidator;
	@Autowired
	private ItemImageRepository itemImageRepository;

	@Value("${minio.bucket}")
	private String bucket;

	@BeforeAll
	void before() throws Exception {
		final String fileName = "No_Image.jpg";

		if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
		}
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
	void validatorTest() {
		// given
		final String noImageUrl = "No_Image.jpg";

		// when
		boolean isOk = imageValidator.validate(noImageUrl);

		// then
		Assertions.assertTrue(isOk);
	}

	@DisplayName("이미지를 상품에 등록한다")
	@Test
	void addImageToItem() {
		// given
		final String fileName = "create.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "create_image".getBytes());
		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});

		final Category category = CategoryFixture.CATEGORY_1.getInstance();

		categoryRepository.save(category);

		final Item item = itemRepository.save(ITEM_1.getInstance(category));
		final String url = imageResponseList.get(0).url();
		final String imageName = url.substring(url.lastIndexOf("/") + 1);

		final List<String> imageNameList = List.of(imageName);
		final ItemImageCreateRequest itemImageCreateRequest = new ItemImageCreateRequest(imageNameList);
		// when
		final List<ItemImageResponse> expected = itemImageService.createItemImage(item.getId(), itemImageCreateRequest);
		final List<ItemImageResponse> actual = itemImageService.findItemImageList(item.getId());
		// then
		Assertions.assertEquals(actual.get(0).imageUrl(), expected.get(0).imageUrl());
	}

	@DisplayName("상품의 이미지를 업데이트한다")
	@Test
	void updateItemImage() {
		// given
		final String fileName = "create.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "create_image".getBytes());
		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});

		final Category category = CategoryFixture.CATEGORY_2.getInstance();

		categoryRepository.save(category);

		final Item item = itemRepository.save(ITEM_1.getInstance(category));
		final String url = imageResponseList.get(0).url();
		final String imageName = url.substring(url.lastIndexOf("/") + 1);

		final ItemImage itemImage = ItemImage.builder().item(item).imageName(imageName).build();
		itemImageRepository.save(itemImage);

		final String beforeChangedImageName = "No_Image.jpg";

		final ItemImageUpdateRequest itemImageUpdateRequest = new ItemImageUpdateRequest(itemImage.getId(),
			beforeChangedImageName);

		// when
		itemImageService.updateItemImage(item.getId(), itemImageUpdateRequest);

		final ItemImage updatedItemImage = itemImageRepository.findByIdAndItemId(itemImageUpdateRequest.imageId(),
				item.getId())
			.orElseThrow(
				() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemImageUpdateRequest.imageId())
			);

		final String actual = updatedItemImage.getImageName();

		// then
		Assertions.assertNotEquals(beforeChangedImageName, actual);
	}

	@DisplayName("상품의 이미지를 제거한다")
	@Test
	void deleteItemImage() {
		// given
		final String fileName = "create.jpg";
		final MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
			"image/jpg", "create image".getBytes());

		final List<ImageResponse> imageResponseList = imageService.uploadImage(new MultipartFile[] {multipartFile});
		final Category category = CategoryFixture.CATEGORY_3.getInstance();

		categoryRepository.save(category);

		final Item item = itemRepository.save(ITEM_1.getInstance(category));
		final String url = imageResponseList.get(0).url();
		final String imageName = url.substring(url.lastIndexOf("/") + 1);

		final ItemImage itemImage = ItemImage.builder().item(item).imageName(imageName).build();
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

	@DisplayName("상품의 썸네일이 유효하지 않으면 예외를 던진다")
	@Test
	void updateItemThumbnail() {
		// given
		Category category = CategoryFixture.CATEGORY_1.getInstance();
		Item item = ITEM_1.getInstance(category);
		categoryRepository.save(category);
		itemRepository.save(item);

		// when
		ItemImage newThumbnail = ItemImage.builder().item(item).imageName("Thumbnail_UUID").build();
		// itemImageRepository.save(newThumbnail);
		ItemThumbnailUpdateRequest itemThumbnailUpdateRequest = new ItemThumbnailUpdateRequest(newThumbnail.getId());

		// then
		assertThatThrownBy(
			() -> itemImageService.updateItemThumbnail(item.getId(), itemThumbnailUpdateRequest)
		);
	}
}
