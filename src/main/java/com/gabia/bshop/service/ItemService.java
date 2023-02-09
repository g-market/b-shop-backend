package com.gabia.bshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.ItemDto;
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

	private final OptionsRepository optionsRepository;

	private final ItemImageRepository itemImageRepository;

	/*
	상품 조회
	* */
	public ItemDto findItem(final Long id) {
		final Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		return ItemMapper.INSTANCE.itemToDto(item);
	}

	/*
	상품 목록 조회
	*/
	public List<ItemDto> findItems(final Pageable page) {
		final Page<Item> itemPage = itemRepository.findAll(page);
		return itemPage.stream().map(ItemMapper.INSTANCE::itemToDto).toList();
	}

	/*
	상품 생성
	*/
	@Transactional
	public ItemDto createItem(final ItemDto itemDto) {

		final Long categoryId = itemDto.categoryDto().id();

		// Validation
		categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

		final Item item = itemRepository.save(ItemMapper.INSTANCE.itemDtoToEntity(itemDto));

		if (!itemDto.optionDtoList().isEmpty()) {
			List<Options> optionsList = itemDto.optionDtoList().stream().map(optionDto -> {
				Options option = OptionMapper.INSTANCE.OptionDtoToEntity(optionDto);
				option.setItem(item);
				return option;
			}).toList();
			optionsRepository.saveAll(optionsList);

		} else {
			Options option = Options.builder()
				.description(itemDto.name()) // 기본 option은 item의 option과 동일
				.optionLevel(1) //
				.item(item)
				.stockQuantity(0)
				.optionPrice(0)
				.build();
			optionsRepository.save(option);
		}

		if (!itemDto.itemImageDtoList().isEmpty()) {
			List<ItemImage> itemImageList = itemDto.itemImageDtoList().stream().map(itemImageDto -> {
				/** TODO
				 1. image url validation
				 (option) 2. file to image url
				**/
				ItemImage itemImage = ItemImageMapper.INSTANCE.ItemImageDtoToEntity(itemImageDto);
				itemImage.setItem(item);
				return itemImage;
			}).toList();
			itemImageRepository.saveAll(itemImageList);
		}

		return ItemMapper.INSTANCE.itemToDto(item);
	}

	/*
	상품 수정
	*/
	@Transactional
	public ItemDto updateItem(final ItemDto itemDto) {
		Item item = itemRepository.findById(itemDto.id()).orElseThrow(EntityNotFoundException::new);
		final Long categoryId = itemDto.categoryDto().id();

		final Category category =
			categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

		item.update(itemDto, category);

		return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
	}

	/*
	상품 삭제
	*/
	@Transactional
	public void deleteItem(final Long id) {
		final Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		itemRepository.deleteById(item.getId());
	}

}
