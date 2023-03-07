package com.gabia.bshop.integration.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.request.CategoryUpdateRequest;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.service.CategoryService;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

	@DisplayName("카테고리를_리스트로_조회한다.")
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
}
