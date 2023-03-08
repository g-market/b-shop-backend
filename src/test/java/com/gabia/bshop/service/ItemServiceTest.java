package com.gabia.bshop.service;

import static com.gabia.bshop.fixture.CategoryFixture.*;
import static com.gabia.bshop.fixture.ItemFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.gabia.bshop.config.ImageDefaultProperties;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ImageDefaultProperties imageDefaultProperties;

	@InjectMocks
	private ItemService itemService;

	@Test
	@DisplayName("상품을_조회한다")
	void findItem() {
		// given
		Category category = CATEGORY_1.getInstance(1L);
		Item item1 = ITEM_1.getInstance(1L, category);
		Item item2 = ITEM_2.getInstance(2L, category);

		// when
		when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
		when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(item2));

		// then
		assertEquals(1L, itemService.findItem(1L).id());
		assertEquals(2L, itemService.findItem(2L).id());
	}

	@Test
	@DisplayName("상품_목록을_조회한다")
	void findItemList() {
		// given
		Category category = CATEGORY_1.getInstance(1L);
		Item item1 = ITEM_1.getInstance(1L, category);
		Item item2 = ITEM_2.getInstance(2L, category);

		Pageable pageable = PageRequest.of(0, 10);
		Page<ItemResponse> itemDtoList = new PageImpl<>(
			Stream.of(item1, item2).map(ItemMapper.INSTANCE::itemToItemResponse).toList());

		Page<Item> itemPage = new PageImpl<>(List.of(item1, item2));

		when(itemRepository.findAll(pageable)).thenReturn(itemPage);

		// when & then
		assertEquals(itemDtoList, itemService.findItemList(pageable, null));
	}

	@Test
	@DisplayName("상품을_수정한다")
	void changeItem() {
		// given
		Category category = CATEGORY_1.getInstance(1L);

		Item item1 = ITEM_1.getInstance(1L, category);
		Item item2 = ITEM_2.getInstance(1L, category);
		ItemUpdateRequest itemDto = ItemMapper.INSTANCE.itemToItemUpdateRequest(item2);

		// when
		when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
		when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));

		// then
		ItemResponse itemResponse = itemService.updateItem(itemDto);

		assertAll(
			() -> assertEquals(20000, itemResponse.basePrice()),
			() -> assertEquals(itemResponse.description(), item2.getDescription()));
	}

	@Test
	@DisplayName("상품을_생성한다")
	void createItem() {
		// given
		Category category = CATEGORY_1.getInstance(1L);

		Item item = ITEM_1.getInstance(category);
		Item returnItem = ITEM_1.getInstance(1L, category);

		// when
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(imageDefaultProperties.getItemImageUrl()).thenReturn("http://localhost:9000/images/No_Image.jpg");
		when(itemRepository.save(any())).thenReturn(returnItem);

		ItemResponse itemResponse = itemService.createItem(ItemMapper.INSTANCE.itemToItemCreateRequest(item));

		// then
		assertAll(
			() -> assertEquals(itemResponse.categoryDto().id(), item.getCategory().getId()),
			() -> assertEquals(itemResponse.name(), item.getName()),
			() -> assertEquals(itemResponse.basePrice(), item.getBasePrice()),
			() -> assertEquals(itemResponse.description(), item.getDescription())
		);

	}

	@Test
	@DisplayName("상품_수정시_상품이_없으면_실패한다")
	void failToDeleteItemWithNoItem() {
		// given
		Category category = CATEGORY_1.getInstance(1L); // 존재하지 않는 카테고리

		Item item = ITEM_3.getInstance(3L, category);

		ItemUpdateRequest itemUpdateRequest = ItemMapper.INSTANCE.itemToItemUpdateRequest(item);

		// when
		when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

		// then
		Assertions.assertThrows(
			NotFoundException.class,
			() -> {
				itemService.updateItem(itemUpdateRequest);
			});
	}

	@Test
	@DisplayName("상품_수정시_카테고리가_없으면_실패한다")
	void failToChangeItemWithoutCategory() {
		// given
		Category category = CATEGORY_1.getInstance(1L); // 존재하지 않는 카테고리
		Item item = ITEM_2.getInstance(1L, category);
		ItemUpdateRequest itemUpdateRequest = ItemMapper.INSTANCE.itemToItemUpdateRequest(item);

		// when
		when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
		when(categoryRepository.findById(1L)).thenThrow(NotFoundException.class);

		// then
		Assertions.assertThrows(
			NotFoundException.class,
			() -> {
				itemService.updateItem(itemUpdateRequest);
			});
	}

	@Test
	@DisplayName("상품_제거에_성공한다")
	void itemDeleteSuccess() {
		// given
		Long itemId1 = 1L;
		Category category = CATEGORY_1.getInstance(1L); // 존재하지 않는 카테고리

		Item item1 = ITEM_1.getInstance(1L, category);
		// when
		when(itemRepository.findById(itemId1)).thenReturn(Optional.ofNullable(item1));
		itemService.deleteItem(itemId1);

		// then
		verify(itemRepository, times(1)).delete(any());
	}

	@Test
	@DisplayName("상품_제거에_실패하면_NotFoundException이_발생한다")
	void failItemDelete() {
		// given
		Long itemId = 3L; // 존재하지 않는 상품

		// when
		when(itemRepository.findById(3L)).thenThrow(NotFoundException.class);

		// then
		Assertions.assertThrows(
			NotFoundException.class,
			() -> {
				itemService.deleteItem(itemId);
			});
	}

	@Test
	@DisplayName("상품 연도를 조회하면 상품에서 존재하는 연도를 다 보여준다")
	void findItemYears() {
		// given
		List<Integer> itemYearList = List.of(2023, 2022, 2021, 2020, 2019);
		when(itemRepository.findItemYears()).thenReturn(itemYearList);

		// when
		List<Integer> result = itemService.findItemYears();

		// then
		assertAll(
			() -> verify(itemRepository).findItemYears(),
			() -> assertThat(result).usingRecursiveComparison().isEqualTo(itemYearList)
		);
	}
}
