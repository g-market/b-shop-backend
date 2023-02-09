package com.gabia.bshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.request.ItemRequestDto;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.Options;
import com.gabia.bshop.mapper.ItemImageMapper;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.mapper.OptionMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.OptionsRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

	private static final int MAX_PAGE_ELEMENT_REQUEST_SIZE = 100;
	/**
	* 상품 조회
	* 1. fetch join
	** */
	public ItemResponse findItem(final Long id) {
		final Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		return ItemMapper.INSTANCE.itemToItemResponse(item);
	}

	/**
	* 상품 목록 조회
	* 1. fetch join
	**/
	public List<ItemResponse> findItemList(final Pageable page) {

		if (page.getPageSize() > MAX_PAGE_ELEMENT_REQUEST_SIZE) {
			throw new IllegalArgumentException(
				String.format("상품은 한 번에 %d개 까지만 조회할 수 있습니다.", MAX_PAGE_ELEMENT_REQUEST_SIZE));
		}

		final Page<Item> itemPage = itemRepository.findAll(page);
		return itemPage.stream().map(ItemMapper.INSTANCE::itemToItemResponse).toList();
	}

	/*
	상품 생성
	*/
	@Transactional
	public ItemResponse createItem(final ItemRequest itemDto) {

		/**
		 * 1. Category 조회
		* */
		final Long categoryId = itemDto.categoryDto().id();
		final Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

		/**
		 * 2. Option 생성
		 * */
		List<Options> optionList = null;
		if (!itemDto.optionDtoList().isEmpty()) {
			optionList = itemDto.optionDtoList().stream().map(OptionMapper.INSTANCE::OptionDtoToEntity).toList();
			// optionsRepository.saveAll(optionsList);
		} else {
			Options option = Options.builder()
				.description(itemDto.name()) // 기본 option은 item의 option과 동일
				.optionLevel(1)
				.stockQuantity(0)
				.optionPrice(0)
				.build();
			optionList = List.of(option);
		}

		/**
		 * 3. Image 생성
		 **/
		List<ItemImage> itemImageList = null;
		if (!itemDto.itemImageDtoList().isEmpty()) {
			itemImageList = itemDto.itemImageDtoList().stream().map(itemImageDto -> {
				/** TODO
				 1. image url validation
				 (option) 2. file to image url
				**/
				ItemImage itemImage = ItemImageMapper.INSTANCE.ItemImageDtoToEntity(itemImageDto);
				return itemImage;
			}).toList();
		}

		/**
		 * 4. Item 생성
		 **/
		final Item item = Item.builder()
			.name(itemDto.name())
			.description(itemDto.description())
			.itemStatus(itemDto.itemStatus())
			.basePrice(itemDto.basePrice())
			.openAt(itemDto.openAt())
			.itemImageList(itemImageList)
			.optionsList(optionList)
			.category(category)
			.deleted(false)
			.build();
		
		
		return ItemMapper.INSTANCE.itemToItemResponse(item);
	}

	/**
	상품 수정
	 1. 상품의 옵션을 수정 ?
	 2. 상품의 카테고리 수정 ?
	 3. 상품의 이미지 수정 ?
	**/
	@Transactional
	public ItemResponse updateItem(final Long itemId ,final ItemRequest itemDto) {
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		final Long categoryId = itemDto.categoryDto().id();

		final Category category =
			categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

		item.update(itemDto, category);

		return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
	}

	/**
	상품 삭제
	 1. 상품 삭제
	 2. 연관된 엔티티 삭제
	*/
	@Transactional
	public void deleteItem(final Long id) {
		final Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		itemRepository.delete(item);
	}

}
