package com.gabia.bshop.service;

import static com.gabia.bshop.fixture.ItemOptionFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.fixture.ItemFixture;
import com.gabia.bshop.mapper.ItemOptionMapper;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemOptionServiceTest {

	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ItemOptionRepository itemOptionRepository;

	@InjectMocks
	private ItemOptionService itemOptionService;

	@Test
	@DisplayName("아이템의 상품 옵션을 검색한다.")
	void findItemOption() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(1L, category1);
		final ItemOption itemOption1 = ITEM_OPTION_1.getInstance(1L, item1);
		final ItemOptionResponse itemOptionResponse = ItemOptionMapper.INSTANCE.itemOptionToResponse(itemOption1);

		given(itemOptionRepository.findByIdAndItemId(itemOption1.getId(), item1.getId())).willReturn(
			Optional.of(itemOption1));

		// when
		final ItemOptionResponse actual = itemOptionService.findItemOption(item1.getId(),
			itemOption1.getId());
		// then
		assertAll(
			() -> assertThat(actual).usingRecursiveComparison().isEqualTo(itemOptionResponse),
			() -> verify(itemOptionRepository).findByIdAndItemId(itemOption1.getId(), item1.getId())
		);

	}

	@Test
	@DisplayName("아이탬의 상품 옵션이 존재하지 않으면 예외가 발생한다")
	void findItemOptionException() {

		// given
		final Long itemId = 1L;
		final Long notExistItemOptionId = 99999L;

		given(itemOptionRepository.findByIdAndItemId(notExistItemOptionId, itemId)).willThrow(NotFoundException.class);

		// when & then
		Assertions.assertThrows(
			NotFoundException.class,
			() -> {
				itemOptionService.findItemOption(itemId, notExistItemOptionId);
			});
	}

	@Test
	@DisplayName("아이탬의 상품 옵션 리스트를 검색한다.")
	void findOptionList() {
		//given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(1L, category1);
		final ItemOption itemOption1 = ITEM_OPTION_1.getInstance(1L, item1);
		final ItemOption itemOption2 = ITEM_OPTION_2.getInstance(1L, item1);
		final List<ItemOption> itemOptionList = List.of(itemOption1, itemOption2);
		final List<ItemOptionResponse> itemOptionResponseList = itemOptionList.stream()
			.map(ItemOptionMapper.INSTANCE::itemOptionToResponse)
			.toList();

		given(itemOptionRepository.findAllByItemId(item1.getId())).willReturn(itemOptionList);

		// when
		final List<ItemOptionResponse> actual = itemOptionService.findOptionList(item1.getId());

		// then
		assertAll(
			() -> assertIterableEquals(itemOptionResponseList, actual),
			() -> verify(itemOptionRepository).findAllByItemId(item1.getId())
		);
	}

	@Test
	@DisplayName("아이탬의 상품 옵션이 없으면 예외가 발생한다.")
	void findOptionListException() {
		//given
		final Long itemId = 99999L;

		given(itemOptionRepository.findAllByItemId(itemId)).willReturn(List.of());

		// when & then
		Assertions.assertThrows(
			NotFoundException.class,
			() -> {
				itemOptionService.findOptionList(itemId);
			});

	}

	@Test
	@DisplayName("아이탬의 상품 옵션을 생성한다.")
	void createItemOption() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(1L, category1);
		final ItemOption itemOption1 = ITEM_OPTION_1.getInstance(1L, item1);

		final ItemOptionResponse itemOptionResponse = ItemOptionMapper.INSTANCE.itemOptionToResponse(itemOption1);

		final ItemOptionRequest itemOptionRequest = new ItemOptionRequest(
			itemOption1.getDescription(),
			itemOption1.getOptionPrice(),
			itemOption1.getStockQuantity());

		given(itemRepository.findById(item1.getId())).willReturn(Optional.of(item1));
		given(itemOptionRepository.save(any())).willReturn(itemOption1);

		// when
		final ItemOptionResponse actual = itemOptionService.createItemOption(item1.getId(), itemOptionRequest);

		// then
		assertThat(actual).usingRecursiveComparison().isEqualTo(itemOptionResponse);
	}

	@Test
	@DisplayName("아이탬 상품 옵션을 변경한다.")
	void changeItemOption() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(1L, category1);
		final ItemOption beforeItemOption = ITEM_OPTION_1.getInstance(1L, item1);
		final ItemOptionResponse changeditemOptionResponse = ItemOptionMapper.INSTANCE.itemOptionToResponse(
			ITEM_OPTION_2.getInstance(1L, item1));

		final ItemOptionRequest itemOptionRequest = new ItemOptionRequest(
			changeditemOptionResponse.description(),
			changeditemOptionResponse.optionPrice(),
			changeditemOptionResponse.stockQuantity()
		);

		given(itemOptionRepository.findByIdAndItemId(beforeItemOption.getId(), item1.getId())).willReturn(
			Optional.of(beforeItemOption));

		// when
		final ItemOptionResponse actual = itemOptionService.changeItemOption(item1.getId(), beforeItemOption.getId(),
			itemOptionRequest);

		// then
		assertAll(
			() -> assertThat(actual).usingRecursiveComparison().isEqualTo(changeditemOptionResponse),
			() -> verify(itemOptionRepository).findByIdAndItemId(beforeItemOption.getId(), item1.getId())
		);
	}

	@Test
	@DisplayName("아이탬 상품 옵션을 변경시 아이탬 옵션이 없으면 예외가 발생한다.")
	void changeItemOptionException() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(1L, category1);

		final Long NotExistItemOptionId = 99999L;
		final ItemOption NotExistItemOption = ITEM_OPTION_1.getInstance(item1);

		final ItemOptionRequest itemOptionRequest = new ItemOptionRequest(
			NotExistItemOption.getDescription(),
			NotExistItemOption.getOptionPrice(),
			NotExistItemOption.getStockQuantity());

		// when & then
		assertThatThrownBy(
			() -> itemOptionService.changeItemOption(item1.getId(), NotExistItemOptionId,
				itemOptionRequest)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("아이탬 상품 옵션을 제거한다")
	void deleteItemOption() {
		// given
		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(1L, category1);
		final ItemOption itemOption1 = ITEM_OPTION_1.getInstance(1L, item1);

		given(itemOptionRepository.findByIdAndItemId(itemOption1.getId(), item1.getId())).willReturn(
			Optional.of(itemOption1));
		willDoNothing().given(itemOptionRepository).delete(itemOption1);

		// when
		itemOptionService.deleteItemOption(item1.getId(), itemOption1.getId());

		// then
		verify(itemOptionRepository).delete(itemOption1);
	}
}
