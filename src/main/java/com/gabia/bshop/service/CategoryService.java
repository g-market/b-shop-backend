package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.request.CategoryUpdateRequest;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.CategoryMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final ItemRepository itemRepository;

	//카테고리 단건 조회
	public CategoryDto findCategory(final Long categoryId) {
		final Category category = findCategoryById(categoryId);

		return CategoryMapper.INSTANCE.categoryToDto(category);
	}

	//카테고리 목록 조회
	public Page<CategoryDto> findCategoryList(final Pageable pageable) {
		final Page<Category> categoryPage = categoryRepository.findAll(pageable);
		return new PageImpl<>(categoryPage.stream().map(CategoryMapper.INSTANCE::categoryToDto).toList());
	}

	//카테고리 생성
	public CategoryDto createCategory(final CategoryCreateRequest categoryCreateRequest) {
		Category category = CategoryMapper.INSTANCE.CategoryRequestToEntity(categoryCreateRequest);
		return CategoryMapper.INSTANCE.categoryToDto(categoryRepository.save(category));
	}

	//카테고리 수정
	public CategoryDto updateCategory(final CategoryUpdateRequest categoryUpdateRequest) {
		final Category category = findCategoryById(categoryUpdateRequest.id());
		category.update(categoryUpdateRequest);
		return CategoryMapper.INSTANCE.categoryToDto(category);
	}

	//카테고리 삭제
	public void deleteCategory(final Long categoryId) {
		final Category category = findCategoryById(categoryId);
		validateDeleteCategoryById(categoryId);

		categoryRepository.delete(category);
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION, categoryId));
	}

	private void validateDeleteCategoryById(final Long categoryId) {
		final List<Item> itemList = itemRepository.findAllByCategoryId(categoryId);
		if (itemList.size() != 0) {
			throw new ConflictException(CATEGORY_ITEM_DELETE_EXCEPTION, categoryId);
		}
	}

}
