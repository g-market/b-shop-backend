package com.gabia.bshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

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

		final Category category =
			categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

		Item item = ItemMapper.INSTANCE.itemDtoToEntity(itemDto);

		return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
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
