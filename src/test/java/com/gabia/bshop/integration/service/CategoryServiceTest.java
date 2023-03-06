package com.gabia.bshop.integration.service;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.request.CategoryUpdateRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.fixture.ItemFixture;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.service.CategoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.gabia.bshop.fixture.ItemOptionFixture.ITEM_OPTION_1;
import static com.gabia.bshop.fixture.ItemOptionFixture.ITEM_OPTION_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
		CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
				.name("category test")
				.build();

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

		categoryRepository.saveAll(List.of(category1,category2,category3));

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
		CategoryUpdateRequest categoryUpdateRequest = CategoryUpdateRequest.builder()
				.id(category1.getId())
				.name("카테고리 수정")
				.build();
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