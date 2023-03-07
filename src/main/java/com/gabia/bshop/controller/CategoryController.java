package com.gabia.bshop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.request.CategoryUpdateRequest;
import com.gabia.bshop.service.CategoryService;
import com.gabia.bshop.util.validator.LimitedSizePagination;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> findCategory(@PathVariable final Long categoryId) {
		return ResponseEntity.ok(categoryService.findCategory(categoryId));
	}

	@GetMapping("/categories")
	public ResponseEntity<Page<CategoryDto>> findCategoryList(@LimitedSizePagination final Pageable pageable) {
		return ResponseEntity.ok(categoryService.findCategoryList(pageable));
	}

	@PostMapping("/categories")
	public ResponseEntity<CategoryDto> createCategory(
		@RequestBody @Valid final CategoryCreateRequest categoryCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryCreateRequest));
	}

	@PatchMapping("/categories")
	public ResponseEntity<CategoryDto> updateCategory(
		@RequestBody @Valid final CategoryUpdateRequest categoryUpdateRequest) {
		return ResponseEntity.ok(categoryService.updateCategory(categoryUpdateRequest));
	}

	@DeleteMapping("/categories/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable final Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.noContent().build();
	}
}
