package com.gabia.bshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
class ItemServiceTest {

	List<Item> itemList;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ItemRepository itemRepository;
	@InjectMocks
	private ItemService itemService;

	@Test
	void 상품_조회() {
		//given
		Category category = Category.builder().id(1L).name("name").build();

		Item item1 =
			Item.builder()
				.id(1L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		Item item2 =
			Item.builder()
				.id(2L)
				.category(category)
				.name("item2")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		// when
		when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
		when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
		when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(item2));

		// then
		assertEquals(1L, itemService.findItem(1L).id());
		assertEquals(2L, itemService.findItem(2L).id());
	}

	@Test
	void 상품_목록_조회() {
		// givn
		Category category = Category.builder().id(1L).name("name").build();

		Item item1 =
			Item.builder()
				.id(1L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		Item item2 =
			Item.builder()
				.id(2L)
				.category(category)
				.name("item2")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		itemList = List.of(item1, item2);

		Pageable pageable = PageRequest.of(0, 10);
		List<ItemDto> itemDtoList = itemList.stream().map(ItemMapper.INSTANCE::itemToDto).toList();

		Page<Item> itemPage = new PageImpl<>(Collections.unmodifiableList(itemList));

		// when
		when(itemRepository.findAll(pageable)).thenReturn(itemPage);

		// then
		assertEquals(itemDtoList, itemService.findItems(pageable));
	}

	@Test
	void 상품_정보_수정() {
		// given
		Category category = Category.builder().id(1L).name("name").build();

		Item item1 =
			Item.builder()
			.id(2L)
			.category(category)
			.name("item2")
			.itemStatus(ItemStatus.PUBLIC)
			.basePrice(10000)
			.description("before")
			.deleted(false)
			.openAt(LocalDateTime.now())
			.build();

		Item item2 =
			Item.builder()
				.id(2L)
				.category(category)
				.name("item2")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(20000)
				.description("changed")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		ItemDto itemDto = ItemMapper.INSTANCE.itemToDto(item2);

		// when
		when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
		when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(item1));
		when(itemRepository.save(item2)).thenReturn(item2);

		// then
		ItemDto changedItem = itemService.updateItem(itemDto);

		assertAll(
			() -> assertEquals(20000, changedItem.basePrice()),
			() -> assertEquals("changed", changedItem.description()));
	}

	@Test
	void 상품_생성() {
		// given
		Category category = Category.builder().id(1L).name("name").build();

		Item item =
			Item.builder()
				.id(1L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(20000)
				.description("item1")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		// when
		when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
		when(itemRepository.save(item)).thenReturn(item);
		ItemDto itemDto = itemService.createItem(ItemMapper.INSTANCE.itemToDto(item));

		// then
		assertAll(
			() -> assertEquals(itemDto.categoryDto().id(), item.getCategory().getId()),
			() -> assertEquals(itemDto.name(), item.getName()),
			() -> assertEquals(itemDto.basePrice(), item.getBasePrice()),
			() -> assertEquals(itemDto.description(), item.getDescription())
		);

	}

	@Test
	void 상품_수정_실패_상품_없음() {
		// given
		Category category = Category.builder().id(1L).name("name").build();

		Item item =
			Item.builder()
				.id(3L)
				.category(category)
				.name("item3")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(20000)
				.description("not exist")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		ItemDto itemDto = ItemMapper.INSTANCE.itemToDto(item);

		// when
		when(itemRepository.findById(3L)).thenThrow(EntityNotFoundException.class);

		// then
		Assertions.assertThrows(
			EntityNotFoundException.class,
			() -> {
				itemService.updateItem(itemDto);
			});
	}

	@Test
	void 상품_수정_실패_카테고리_없음() {
		// given
		Category category = Category.builder().id(2L).name("name").build(); // 존재하지 않는 카테고리

		Item item =
			Item.builder()
				.id(2L)
				.category(category)
				.name("item2")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(20000)
				.description("changed")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		ItemDto itemDto = ItemMapper.INSTANCE.itemToDto(item);

		// when
		when(categoryRepository.findById(2L)).thenThrow(EntityNotFoundException.class);

		// then
		Assertions.assertThrows(
			EntityNotFoundException.class,
			() -> {
				itemService.updateItem(itemDto);
			});
	}

	@Test
	void 상품_제거_성공() {
		// given
		Long itemId1 = 1L;
		Long itemId2 = 2L;

		Category category = Category.builder().id(1L).name("name").build(); // 존재하지 않는 카테고리

		Item item1 =
			Item.builder()
				.id(itemId1)
				.category(category)
				.name("item2")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(20000)
				.description("changed")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		Item item2 =
			Item.builder()
				.id(itemId2)
				.category(category)
				.name("item2")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(20000)
				.description("changed")
				.deleted(false)
				.openAt(LocalDateTime.now())
				.build();

		// when
		when(itemRepository.findById(itemId1)).thenReturn(Optional.ofNullable(item1));
		when(itemRepository.findById(itemId2)).thenReturn(Optional.ofNullable(item2));

		itemService.deleteItem(itemId1);
		itemService.deleteItem(itemId2);

		// then
		verify(itemRepository, times(2)).deleteById(anyLong());
	}

	@Test
	void 상품_제거_실패() {
		// given
		Long itemId = 3L; // 존재하지 않는 상품

		// when
		when(itemRepository.findById(3L)).thenThrow(EntityNotFoundException.class);

		// then
		Assertions.assertThrows(
			EntityNotFoundException.class,
			() -> {
				itemService.deleteItem(3L);
			});
	}
}
