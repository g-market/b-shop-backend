package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.request.CategoryUpdateRequest;
import com.gabia.bshop.dto.response.CategoryAllInfoResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.CategoryMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final ItemRepository itemRepository;

	public CategoryDto findCategory(final Long categoryId) {
		final Category category = findCategoryById(categoryId);
		if (category.isDeleted()) {
			throw new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION, category.getId());
		}
		return CategoryMapper.INSTANCE.categoryToDto(category);
	}

	public Page<CategoryDto> findCategoryList(final Pageable pageable) {
		return categoryRepository.findCategoryList(pageable).map(CategoryMapper.INSTANCE::categoryToDto);
	}

	public CategoryAllInfoResponse findCategoryWithDeleted(final Long categoryId) {
		final Category category = findCategoryById(categoryId);
		return CategoryMapper.INSTANCE.categoryToCategoryAllInfoResponse(category);
	}

	public Page<CategoryAllInfoResponse> findCategoryListWithDeleted(final Pageable pageable) {
		return categoryRepository.findCategoryListWithDeleted(pageable)
			.map(CategoryMapper.INSTANCE::categoryToCategoryAllInfoResponse);
	}

	@CacheEvict(key = "0", value = "findCategoryNames")
	@Transactional
	public CategoryDto createCategory(final CategoryCreateRequest categoryCreateRequest) {
		existCategoryByName(categoryCreateRequest.name());
		Category category = CategoryMapper.INSTANCE.CategoryRequestToEntity(categoryCreateRequest);
		return CategoryMapper.INSTANCE.categoryToDto(categoryRepository.save(category));
	}

	@CacheEvict(key = "0", value = "findCategoryNames")
	@Transactional
	public CategoryDto updateCategory(final CategoryUpdateRequest categoryUpdateRequest) {
		existCategoryByName(categoryUpdateRequest.name());
		final Category category = findCategoryById(categoryUpdateRequest.id());
		category.update(categoryUpdateRequest);
		return CategoryMapper.INSTANCE.categoryToDto(category);
	}

	@CacheEvict(key = "0", value = "findCategoryNames")
	@Transactional
	public void deleteCategory(final Long categoryId) {
		final Category category = findCategoryById(categoryId);
		validateDeleteCategoryById(categoryId);

		categoryRepository.delete(category);
	}

	@Cacheable(key = "0", value = "findCategoryNames")
	public List<String> findCategoryNames() {
		return categoryRepository.findCategoryNames();
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION, categoryId));
	}

	private void existCategoryByName(final String categoryName) {
		final boolean isExistCategoryName = categoryRepository.existsByName(categoryName);
		if (isExistCategoryName) {
			throw new ConflictException(CATEGORY_NAME_UNIQUE_EXCEPTION, categoryName);
		}
	}

	private void validateDeleteCategoryById(final Long categoryId) {
		final List<Item> itemList = itemRepository.findAllByCategoryId(categoryId);
		if (itemList.size() != 0) {
			throw new ConflictException(CATEGORY_ITEM_DELETE_EXCEPTION, categoryId);
		}
	}
}
