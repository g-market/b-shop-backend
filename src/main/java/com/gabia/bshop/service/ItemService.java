package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.config.ImageDefaultProperties;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.request.ItemCreateRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemAllInfoResponse;
import com.gabia.bshop.dto.response.ItemPageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.dto.searchConditions.ItemSearchConditions;
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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final ImageValidator imageValidator;
	private final ImageDefaultProperties imageDefaultProperties;

	/**
	 * 상품 조회
	 * 1. fetch join
	 ** */
	public ItemResponse findItem(final Long id) {
		final Item item = findItemById(id);
		//PRIVATE인 상품은 일반 조회 불가능
		if (item.getItemStatus() == ItemStatus.PRIVATE || item.isDeleted()) {
			throw new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, item.getId());
		}
		return ItemMapper.INSTANCE.itemToItemResponse(item);
	}

	/**
	 * 상품 목록 조회
	 * 비활성, Private 상태의 아이템?
	 *
	 **/
	public Page<ItemPageResponse> findItemListByItemSearchConditions(final Pageable pageable,
		final ItemSearchConditions itemSearchConditions) {
		return itemRepository.findItemListByItemSearchConditions(pageable, itemSearchConditions)
			.map(ItemMapper.INSTANCE::itemToItemPageResponse);
	}

	public ItemAllInfoResponse findItemWithDeleted(final Long itemId) {
		final Item item = itemRepository.findById(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId)
		);
		return ItemMapper.INSTANCE.itemToItemAllInfoResponse(item);
	}

	public Page<ItemAllInfoResponse> findItemListWithDeleted(final Pageable pageable,
		final ItemSearchConditions itemSearchConditions) {
		return itemRepository.findItemListWithDeletedByItemSearchConditions(pageable, itemSearchConditions)
			.map(ItemMapper.INSTANCE::itemToItemAllInfoResponse);
	}

	/**
	 * 상품 생성
	 **/
	@Transactional
	public ItemResponse createItem(final ItemCreateRequest itemCreateRequest) {

		// 1. Category 조회
		final Long categoryId = itemCreateRequest.categoryId();
		final Category category = findCategoryById(categoryId);

		// 2. Item 생성
		final Item item = Item.builder()
			.name(itemCreateRequest.name())
			.description(itemCreateRequest.description())
			.basePrice(itemCreateRequest.basePrice())
			.category(category)
			.openAt(getOpenedAt(itemCreateRequest.openAt()))
			.year(itemCreateRequest.year())
			.itemStatus(getItemStatus(itemCreateRequest.itemStatus()))
			.build();

		// 3. Option 생성
		if (itemCreateRequest.itemOptionDtoList() != null && !itemCreateRequest.itemOptionDtoList().isEmpty()) {
			itemCreateRequest.itemOptionDtoList()
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
			final ItemOption itemOption = ItemOption.builder()
				.item(item)
				.description(itemCreateRequest.name()) // 기본 option은 item의 option과 동일
				.stockQuantity(0)
				.optionPrice(0)
				.build();
			item.addItemOption(itemOption);
		}

		// 4. Image 생성
		if (itemCreateRequest.itemImageDtoList() != null && !itemCreateRequest.itemImageDtoList().isEmpty()) {
			for (ItemImageDto itemImageDto : itemCreateRequest.itemImageDtoList()) {
				final boolean isValid = imageValidator.validate(itemImageDto.imageUrl());

				if (!isValid) {
					throw new NotFoundException(INCORRECT_URL_EXCEPTION);
				}

				final String imageName = itemImageDto.imageUrl()
					.substring(itemImageDto.imageUrl().lastIndexOf("/") + 1);

				item.addItemImage(ItemImage.builder()
					.item(item)
					.imageName(imageName)
					.build());
			}
		} else {
			final ItemImage itemImage = ItemImage.builder()
				.item(item)
				.imageName(imageDefaultProperties.getItemImageUrl())
				.build();
			item.addItemImage(itemImage);
		}
		// 6. 썸네일 설정
		item.updateThumbnail(item.getItemImageList().get(0).getImageName()); // 0 번째 이미지를 썸네일로

		return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(item));
	}

	/**
	 상품 수정
	 1. 상품의 카테고리 수정 -> Category 변경
	 2. 이름(name)
	 3. 기본가격(basePrice)
	 4. 설명(itemOptionDescription)
	 5. 오픈시간(openAt)
	 6. 년도(year)
	 7. 상태(status)
	 **/
	@Transactional
	public ItemResponse updateItem(final ItemUpdateRequest itemUpdateRequest) {
		final Item item = findItemById(itemUpdateRequest.itemId());
		final Category category;
		if (itemUpdateRequest.categoryId() == null) {
			category = item.getCategory();
		} else {
			category = findCategoryById(itemUpdateRequest.categoryId());
		}

		item.update(itemUpdateRequest, category);

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

	public List<Integer> findItemYears() {
		return itemRepository.findItemYears();
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
