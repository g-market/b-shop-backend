package com.gabia.bshop.integration.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.request.CategoryUpdateRequest;
import com.gabia.bshop.dto.response.CategoryAllInfoResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.service.CategoryService;

@Transactional
@SpringBootTest
class CategoryServiceTest extends IntegrationTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@DisplayName("카테고리를_생성한다.")
	@Test
	void createCategory() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);

		//when
		CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("category test");
		CategoryDto categoryDto = categoryService.createCategory(categoryCreateRequest);

		//then
		Assertions.assertThat(categoryDto.name()).isEqualTo(categoryCreateRequest.name());
	}

	@DisplayName("카테고리를 생성할 때 존재하는 이름이면 예외를 던진다.")
	@Test
	void createDuplicatedCategory() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);

		// when
		CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(category1.getName());

		//when & then
		Assertions.assertThatThrownBy(
			() -> categoryService.createCategory(categoryCreateRequest)
		);
	}

	@DisplayName("카테고리를_단건으로_조회한다.")
	@Test
	void findCategory() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);

		//when
		Category category = categoryRepository.findById(category1.getId()).orElseThrow();
		CategoryDto categoryDto = categoryService.findCategory(category.getId());

		//then
		Assertions.assertThat(categoryDto.name()).isEqualTo(category1.getName());
		Assertions.assertThat(categoryDto.id()).isEqualTo(category1.getId());
	}

	@DisplayName("일반 사용자는 삭제된 카테고리를 단건으로 조회 하지 못한다.")
	@Test
	void findCategoryWhenNormalUser() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);
		categoryRepository.delete(category1);

		// when & then
		Assertions.assertThatThrownBy(
			() -> categoryService.findCategory(category1.getId())
		);
	}

	@DisplayName("관리자는 삭제된 카테고리를 단건으로 조회한다.")
	@Test
	void findCategoryWhenAdmin() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);
		categoryRepository.delete(category1);
		categoryRepository.flush();

		// when
		CategoryAllInfoResponse response = categoryService.findCategoryWithDeleted(category1.getId());

		// then
		assertAll(
			() -> Assertions.assertThat(response.id()).isEqualTo(category1.getId()),
			() -> Assertions.assertThat(response.name()).isEqualTo(category1.getName()),
			() -> Assertions.assertThat(response.deleted()).isEqualTo(true)
		);

	}

	@DisplayName("관리자는 삭제된 카테고리를 리스트로 조회한다.")
	@Test
	void findCategoryListWithDeleted() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Category category2 = CategoryFixture.CATEGORY_2.getInstance();
		final Category category3 = CategoryFixture.CATEGORY_3.getInstance();
		categoryRepository.saveAll(List.of(category1, category2, category3));
		categoryRepository.deleteAll(List.of(category1, category2, category3));
		categoryRepository.flush();

		final CategoryAllInfoResponse categoryAllInfoResponse1 = new CategoryAllInfoResponse(category1.getId(),
			category1.getName(), true);
		final CategoryAllInfoResponse categoryAllInfoResponse2 = new CategoryAllInfoResponse(category2.getId(),
			category2.getName(), true);
		final CategoryAllInfoResponse categoryAllInfoResponse3 = new CategoryAllInfoResponse(category3.getId(),
			category3.getName(), true);

		// when
		Pageable pageable = PageRequest.ofSize(12);
		Page<CategoryAllInfoResponse> response = categoryService.findCategoryListWithDeleted(pageable);
		List<CategoryAllInfoResponse> content = response.getContent();

		// then
		assertAll(
			() -> Assertions.assertThat(content).usingRecursiveComparison().isEqualTo(List.of(categoryAllInfoResponse1,
				categoryAllInfoResponse2, categoryAllInfoResponse3))
		);
	}

	@DisplayName("카테고리를_페이지로_조회한다.")
	@Test
	void findCategoryList() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Category category2 = CategoryFixture.CATEGORY_2.getInstance();
		final Category category3 = CategoryFixture.CATEGORY_3.getInstance();

		categoryRepository.saveAll(List.of(category1, category2, category3));
		//when
		Pageable pageable = PageRequest.of(0, 10);
		Page<CategoryDto> categoryDtoPage = categoryService.findCategoryList(pageable);

		//then
		List<CategoryDto> categoryDtoList = categoryDtoPage.getContent();
		Assertions.assertThat(categoryDtoList.get(0).name()).isEqualTo("카테고리 1");
		Assertions.assertThat(categoryDtoList.get(1).name()).isEqualTo("카테고리 2");
		Assertions.assertThat(categoryDtoList.get(2).name()).isEqualTo("카테고리 3");
	}

	@DisplayName("카테고리를_수정한다.")
	@Test
	void updateCategory() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);

		//when
		CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(category1.getId(), "category update");
		CategoryDto categoryDto = categoryService.updateCategory(categoryUpdateRequest);

		//then
		Assertions.assertThat(categoryDto.name()).isEqualTo(categoryUpdateRequest.name());
		Assertions.assertThat(categoryDto.id()).isEqualTo(categoryUpdateRequest.id());
	}

	@DisplayName("카테고리를_삭제한다.")
	@Test
	void deleteCategory() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		categoryRepository.save(category1);

		//when
		categoryService.deleteCategory(category1.getId());

		//then
		Assertions.assertThat(categoryRepository.findById(category1.getId()).isPresent()).isEqualTo(false);
	}

	@DisplayName("카테고리 이름을 리스트로 조회한다.")
	@Test
	void findCategoryNames() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Category category2 = CategoryFixture.CATEGORY_2.getInstance();
		final Category category3 = CategoryFixture.CATEGORY_3.getInstance();
		final Category category4 = CategoryFixture.CATEGORY_4.getInstance();
		final Category category5 = CategoryFixture.CATEGORY_5.getInstance();
		categoryRepository.saveAll(List.of(category1, category2, category3, category4, category5));

		// when
		final List<String> categoryNames = categoryService.findCategoryNames();

		// then
		Assertions.assertThat(categoryNames)
			.usingRecursiveComparison()
			.isEqualTo(List.of(category1.getName(), category2.getName(), category3.getName(), category4.getName(),
				category5.getName()));
	}
}
