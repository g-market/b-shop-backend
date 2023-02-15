package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.ItemChangeRequest;
import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemImageMapper;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.mapper.ItemOptionMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final ItemOptionRepository itemOptionRepository;

	private static final int MAX_PAGE_ELEMENT_REQUEST_SIZE = 100;
	private static final String NO_IMAGE_URL = "TO_BE_CHANGE";

	/**
	 * 상품 조회
	 * 1. fetch join
	 ** */
	public ItemResponse findItem(final Long id) {
		final Item item = itemRepository.findById(id).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, id)
		);

		return ItemMapper.INSTANCE.itemToItemResponse(item);
	}

	/**
	 * 상품 목록 조회
	 * 1. fetch join
	 *
	 **/
	public List<ItemResponse> findItemList(final Pageable page) {

		if (page.getPageSize() > MAX_PAGE_ELEMENT_REQUEST_SIZE) {
			throw new ConflictException(MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION, MAX_PAGE_ELEMENT_REQUEST_SIZE);
		}

		final Page<Item> itemPage = itemRepository.findAll(page);
		return itemPage.stream().map(ItemMapper.INSTANCE::itemToItemResponse).toList();
	}

	/*
	상품 생성
	*/
	@Transactional
	public ItemResponse createItem(final ItemRequest itemDto) {


		// 1. Category 조회
		final Long categoryId = itemDto.categoryDto().id();
		final Category category = findCategoryById(categoryId);

		//2. Option 생성
		List<ItemOption> itemOptionList = null;
		if (itemDto.itemOptionDtoList() != null && !itemDto.itemOptionDtoList().isEmpty()) {
			itemOptionList = itemDto.itemOptionDtoList()
				.stream()
				.map(ItemOptionMapper.INSTANCE::ItemOptionDtoToEntity)
				.toList();
		} else {
			ItemOption itemOption = ItemOption.builder()
				.description(itemDto.name()) // 기본 option은 item의 option과 동일
				.stockQuantity(0)
				.optionPrice(0)
				.build();
			itemOptionList = List.of(itemOption);
		}

		// 3. Image 생성
		List<ItemImage> itemImageList = null;
		if (itemDto.itemImageDtoList() != null && !itemDto.itemImageDtoList().isEmpty()) {
			/** TODO
			 1. image url Validation
			 2. (option) file to image url
			 **/
			itemImageList = itemDto.itemImageDtoList()
				.stream()
				.map(ItemImageMapper.INSTANCE::ItemImageDtoToEntity)
				.toList();
		} else {
			ItemImage itemImage = ItemImage.builder()
				.url(NO_IMAGE_URL)
				.build();
			itemImageList = List.of(itemImage);
		}

		// 4. Item 생성
		final Item item = Item.builder()
			.name(itemDto.name())
			.description(itemDto.description())
			.basePrice(itemDto.basePrice())
			.itemImageList(itemImageList)
			.itemOptionList(itemOptionList)
			.category(category)
			.openAt(getOpenedAt(itemDto.openAt()))
			.itemStatus(getItemStatus(itemDto.itemStatus()))
			.build();

		return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(item));
	}

	/**
	 상품 수정
	 1. 상품의 카테고리 수정 -> Category 변경
	 2. 이름(name)
	 3. 기본가격(basePrice)
	 4. 설명(description)
	 5.
	 **/
	@Transactional
	public ItemResponse updateItem(final ItemChangeRequest itemChangeRequest) {
		Item item = findItemById(itemChangeRequest.itemId());
		final Long categoryId = itemChangeRequest.categoryDto().id();

		final Category category = findCategoryById(categoryId);

		item.update(itemChangeRequest, category);

		return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(item));
	}

	/**
	 상품 삭제
	 1. 상품 삭제
	 2. 연관된 엔티티 삭제
	 3. TODO : 연관된 이미지 삭제
	 **/
	@Transactional
	public void deleteItem(final Long id) {
		final Item item = findItemById(id);
		itemOptionRepository.deleteAllByItem_Id(id);
		itemRepository.delete(item);
	}

	private Item findItemById(final Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION, categoryId));
	}

	private LocalDateTime getOpenedAt(LocalDateTime localDateTime) {
		return localDateTime == null ? LocalDateTime.now() : localDateTime;
	}

	private ItemStatus getItemStatus(ItemStatus itemStatus) {
		return itemStatus == null ? ItemStatus.PRIVATE : itemStatus;
	}
}
