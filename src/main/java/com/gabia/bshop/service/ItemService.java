package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.request.ItemChangeRequest;
import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.util.ImageValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final ImageValidator imageValidator;
	@Value("${minio.default.image}")
	private String NO_IMAGE_URL;

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
	 * 비활성, Private 상태의 아이템?
	 *
	 **/
	public Page<ItemResponse> findItemList(final Pageable page, final Long categoryId) {
		Page<Item> itemPage;
		if (categoryId == null) {
			itemPage = itemRepository.findAll(page);
		} else {
			final Category category = findCategoryById(categoryId);
			itemPage = itemRepository.findByCategory(category, page);
		}
		return new PageImpl<>(itemPage.stream().map(ItemMapper.INSTANCE::itemToItemResponse).toList());
	}

	/*
	상품 생성
	*/
	@Transactional
	public ItemResponse createItem(final ItemRequest itemDto) {

		// 1. Category 조회
		final Long categoryId = itemDto.categoryId();
		final Category category = findCategoryById(categoryId);

		// 2. Item 생성
		final Item item = Item.builder()
			.name(itemDto.name())
			.description(itemDto.description())
			.basePrice(itemDto.basePrice())
			.category(category)
			.openAt(getOpenedAt(itemDto.openAt()))
			.year(itemDto.year())
			.itemStatus(getItemStatus(itemDto.itemStatus()))
			.build();

		// 3. Option 생성
		if (itemDto.itemOptionDtoList() != null && !itemDto.itemOptionDtoList().isEmpty()) {
			itemDto.itemOptionDtoList()
				.stream()
				.map(
					itemOptionDto -> ItemOption.builder()
						.item(item)
						.description(itemOptionDto.description())
						.stockQuantity(itemOptionDto.stockQuantity())
						.optionPrice(itemOptionDto.optionPrice())
						.build()
				).forEach(item::addItemOption);
		} else {
			ItemOption itemOption = ItemOption.builder()
				.item(item)
				.description(itemDto.name()) // 기본 option은 item의 option과 동일
				.stockQuantity(0)
				.optionPrice(0)
				.build();
			item.addItemOption(itemOption);
		}

		// 4. Image 생성
		if (itemDto.itemImageDtoList() != null && !itemDto.itemImageDtoList().isEmpty()) {
			for (ItemImageDto itemImageDto : itemDto.itemImageDtoList()) {
				final boolean isValid = imageValidator.validate(itemImageDto.url());

				if (!isValid) {
					throw new NotFoundException(INCORRECT_URL_EXCEPTION);
				}
				item.addItemImage(ItemImage.builder()
					.item(item)
					.url(itemImageDto.url())
					.build());
			}
		} else {
			ItemImage itemImage = ItemImage.builder()
				.item(item)
				.url(NO_IMAGE_URL)
				.build();
			item.addItemImage(itemImage);
		}
		// 6. 썸네일 설정
		item.setThumbnail(item.getItemImageList().get(0)); // 0 번째 이미지를 썸네일로

		return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(item));
	}

	/**
	 상품 수정
	 1. 상품의 카테고리 수정 -> Category 변경
	 2. 이름(name)
	 3. 기본가격(basePrice)
	 4. 설명(description)
	 5. 오픈시간(openAt)
	 6. 년도(year)
	 7. 상태(status)
	 **/
	@Transactional
	public ItemResponse updateItem(final ItemChangeRequest itemChangeRequest) {
		Item item = findItemById(itemChangeRequest.itemId());
		final Long categoryId = itemChangeRequest.categoryId();

		final Category category = findCategoryById(categoryId);

		item.update(itemChangeRequest, category);

		return ItemMapper.INSTANCE.itemToItemResponse(item);
	}

	/**
	 상품 삭제
	 **/
	@Transactional
	public void deleteItem(final Long id) {
		final Item item = findItemById(id);
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
