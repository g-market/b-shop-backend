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
import com.gabia.bshop.dto.request.CategoryRequest;
import com.gabia.bshop.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> findCategory(@PathVariable final Long categoryId) {
		return ResponseEntity.ok().body(categoryService.findCategory(categoryId));
	}

	@GetMapping("/categories")
	public ResponseEntity<Page<CategoryDto>> findCategoryList(final Pageable pageable) {
		return ResponseEntity.ok().body(categoryService.findCategoryList(pageable));
	}

	@PostMapping("/categories")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody final CategoryRequest categoryRequest) {
		return ResponseEntity.ok().body(categoryService.createCategory(categoryRequest));
	}

	@PatchMapping("/categories")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody final CategoryDto categoryDto) {
		return ResponseEntity.ok().body(categoryService.updateCategory(categoryDto));
	}

	@DeleteMapping("/categories/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable final Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
