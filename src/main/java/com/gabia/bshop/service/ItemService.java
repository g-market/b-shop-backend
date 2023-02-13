package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;

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
		final Item item = findItemById(id);

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

		findCategoryById(categoryId);

		Item item = ItemMapper.INSTANCE.itemDtoToEntity(itemDto);

		return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
	}

	/*
	상품 수정
	*/
	@Transactional
	public ItemDto updateItem(final ItemDto itemDto) {
		final Long itemId = itemDto.id();
		Item item = findItemById(itemId);
		final Long categoryId = itemDto.categoryDto().id();

		final Category category = findCategoryById(categoryId);

		item.update(itemDto, category);

		return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
	}

	/*
	상품 삭제
	*/
	@Transactional
	public void deleteItem(final Long id) {
		final Item item = findItemById(id);
		itemRepository.deleteById(item.getId());
	}

	private Item findItemById(final Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION, categoryId));
	}
}
