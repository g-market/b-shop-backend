package com.gabia.bshop.integration.service;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.fixture.ItemFixture;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.service.CategoryService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gabia.bshop.fixture.ItemOptionFixture.ITEM_OPTION_1;
import static com.gabia.bshop.fixture.ItemOptionFixture.ITEM_OPTION_2;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryServiceTest extends IntegrationTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private EntityManager entityManager;

	private final Long itemId = 1L;
	private final Long itemOptionId = 1L;
	private final Long newItemId = 2L;
	private final Long newItemOptionId = 2L;

	@DisplayName("카테고리를_생성한다.")
	@Test
	void createCategory() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(itemId, category1);
		final ItemOption itemOption1 = ITEM_OPTION_1.getInstance(itemOptionId, item1);
		final Item item2 = ItemFixture.ITEM_2.getInstance(newItemId, category1);
		final ItemOption itemOption2 = ITEM_OPTION_2.getInstance(newItemOptionId, item2);

		categoryRepository.save(category1);
		itemRepository.saveAll(List.of(item1, item2));
		itemOptionRepository.saveAll(List.of(itemOption1, itemOption2));

		//entityManager.clear();

		//when
		CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
				.name("category test")
				.build();

		CategoryDto categoryDto = categoryService.createCategory(categoryCreateRequest);
		//then
		Assertions.assertThat(categoryDto.name()).isEqualTo(categoryCreateRequest.name());
	}
}