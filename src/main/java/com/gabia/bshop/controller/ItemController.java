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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.service.ItemService;
import com.gabia.bshop.util.validator.LimitedSizePagination;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;

	@GetMapping("/items/{id}")
	public ResponseEntity<ItemResponse> findItem(@PathVariable final Long id) {
		return ResponseEntity.ok().body(itemService.findItem(id));
	}

	@GetMapping("/items")
	public ResponseEntity<Page<ItemResponse>> findItemList(
		@LimitedSizePagination final Pageable pageable,
		@RequestParam("categoryId") final Long categoryId) {
		return ResponseEntity.ok().body(itemService.findItemList(pageable, categoryId));
	}

	@Login(admin = true)
	@PostMapping("/items")
	public ResponseEntity<ItemResponse> createItem(@RequestBody @Valid final ItemRequest itemRequest) {
		return ResponseEntity.ok().body(itemService.createItem(itemRequest));
	}

	@Login(admin = true)
	@PatchMapping("/items")
	public ResponseEntity<ItemResponse> updateItem(@RequestBody @Valid final ItemUpdateRequest itemUpdateRequest) {
		return ResponseEntity.ok().body(itemService.updateItem(itemUpdateRequest));
	}

	@Login(admin = true)
	@DeleteMapping("/items/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable final Long id) {
		itemService.deleteItem(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
