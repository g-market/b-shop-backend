package com.gabia.bshop.service;

import static com.gabia.bshop.fixture.CategoryFixture.*;
import static com.gabia.bshop.fixture.ItemFixture.*;
import static com.gabia.bshop.fixture.ItemOptionFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.dto.request.ItemCreateRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemPageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.dto.searchConditions.ItemSearchConditions;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.util.ImageValidator;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ImageDefaultProperties imageDefaultProperties;
	@Mock
	private ImageValidator imageValidator;

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
		assertEquals(1L, itemService.findItem(1L).itemId());
		assertEquals(2L, itemService.findItem(2L).itemId());
	}

	@Test
	@DisplayName("검색 조건 없이 게시글을 검색하면 게시글 페이지를 반환한다")
	void findItemListWithoutSearchConditions() {
		// given
		Category category = CATEGORY_1.getInstance(1L);
		Item item1 = ITEM_1.getInstance(1L, category);
		Item item2 = ITEM_2.getInstance(2L, category);
		Item item3 = ITEM_3.getInstance(3L, category);
		Item item4 = ITEM_4.getInstance(4L, category);
		Item item5 = ITEM_5.getInstance(5L, category);

		List<Item> expectedItemList = List.of(item1, item2, item3, item4, item5);
		List<ItemPageResponse> expected = expectedItemList.stream()
			.map(ItemMapper.INSTANCE::itemToItemPageResponse)
			.toList();

		Pageable pageable = PageRequest.ofSize(12);
		when(itemRepository.findItemListByItemSearchConditions(pageable, null))
			.thenReturn(new PageImpl<>(expectedItemList, pageable, 5));

		// when
		Page<ItemPageResponse> itemPageResponsePage = itemService.findItemListByItemSearchConditions(pageable,
			null);

		// then
		assertThat(itemPageResponsePage.getContent()).usingRecursiveComparison().isEqualTo(expected);
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
		when(imageDefaultProperties.getItemImageUrl()).thenReturn("default-item-image.jpg");
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
	@DisplayName("상품을 아이템 옵션과 함께 생성한다")
	void createItemWithOption() {
		// given
		Category category = CATEGORY_1.getInstance(1L);

		Item item = ITEM_1.getInstance(category);
		ItemOption itemOption1 = ITEM_OPTION_1.getInstance(item);
		ItemOption itemOption2 = ITEM_OPTION_2.getInstance(item);
		ItemOption itemOption3 = ITEM_OPTION_3.getInstance(item);
		List.of(itemOption1, itemOption2, itemOption3).forEach(item::addItemOption);
		ItemImage itemImage1 = ItemImage.builder()
			.item(item)
			.imageName("http://localhost:9000/images/item-image1.jpg")
			.build();
		ItemImage itemImage2 = ItemImage.builder()
			.item(item)
			.imageName("http://localhost:9000/images/item-image2.jpg")
			.build();
		List.of(itemImage1, itemImage2).forEach(item::addItemImage);

		Item returnItem = ITEM_1.getInstance(1L, category);

		// when
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(itemRepository.save(any())).thenReturn(returnItem);
		when(imageValidator.validate(any())).thenReturn(true);

		ItemCreateRequest itemCreateRequest = ItemCreateRequest.builder()
			.categoryId(category.getId())
			.itemOptionDtoList(List.of(
				ItemOptionDto.builder()
					.description(itemOption1.getDescription())
					.optionPrice(itemOption1.getOptionPrice())
					.stockQuantity(itemOption1.getStockQuantity())
					.build(),
				ItemOptionDto.builder()
					.description(itemOption2.getDescription())
					.optionPrice(itemOption2.getOptionPrice())
					.stockQuantity(itemOption2.getStockQuantity())
					.build()
			))
			.itemImageDtoList(List.of(
				ItemImageDto.builder()
					.imageId(1L)
					.imageUrl("http://localhost:9000/images/item-image1.jpg")
					.build(),
				ItemImageDto.builder()
					.imageId(2L)
					.imageUrl("http://localhost:9000/images/item-image2.jpg")
					.build()
			))
			.build();

		ItemResponse itemResponse = itemService.createItem(itemCreateRequest);

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

	@Test
	@DisplayName("삭제한 상품을 단건 조회한다")
	void findDeletedItem() {
		// given
		Long itemId = 1L;
		Category category = CATEGORY_1.getInstance(1L);
		Item item1 = ITEM_1.getInstance(1L, category);
		// when
		when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));
		itemService.findItemWithDeleted(itemId);

		// then
		verify(itemRepository, times(1)).findById(itemId);
	}

	@Test
	@DisplayName("삭제한 상품을 여러개를 조회한다")
	void findDeletedItemList() {
		// given
		Category category = CATEGORY_1.getInstance(1L);
		Item item1 = ITEM_1.getInstance(1L, category);
		Item item2 = ITEM_2.getInstance(2L, category);
		Item item3 = ITEM_3.getInstance(3L, category);
		Item item4 = ITEM_4.getInstance(4L, category);
		Item item5 = ITEM_5.getInstance(5L, category);
		PageRequest pageable = PageRequest.of(0, 10);
		Page<Item> itemPage = new PageImpl<>(List.of(item1, item2, item3, item4, item5), pageable, 5);
		ItemSearchConditions itemSearchConditions = new ItemSearchConditions(null, null, null);
		when(itemRepository.findItemListWithDeletedByItemSearchConditions(pageable,
			itemSearchConditions)).thenReturn(itemPage);

		// when
		itemService.findItemListWithDeleted(pageable, itemSearchConditions);

		// then
		verify(itemRepository, times(1)).findItemListWithDeletedByItemSearchConditions(pageable,
			itemSearchConditions);
	}

	@Test
	@DisplayName("상품 조회시 PRIVATE면 예외를 던진다")
	void findPrivateItem() {
		// given
		Long itemId = 1L;
		Category category = CATEGORY_1.getInstance(1L);
		Item item4 = ITEM_4.getInstance(1L, category);
		// when
		when(itemRepository.findById(itemId)).thenReturn(Optional.of(item4));

		assertThatThrownBy(
			() -> itemService.findItem(itemId)
		);
	}
}
