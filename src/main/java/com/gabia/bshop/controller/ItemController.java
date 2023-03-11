package com.gabia.bshop.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.request.ItemCreateRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemPageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.dto.searchConditions.ItemSearchConditions;
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
		return ResponseEntity.ok(itemService.findItem(id));
	}

	@GetMapping("/items")
	public ResponseEntity<Page<ItemPageResponse>> findItemList(@LimitedSizePagination final Pageable pageable,
		final ItemSearchConditions itemSearchConditions) {
		return ResponseEntity.ok(itemService.findItemListByItemSearchConditions(pageable, itemSearchConditions));
	}

	@Login(admin = true)
	@GetMapping("/admin/items/{id}")
	public ResponseEntity<ItemResponse> findItemWithDeleted(@PathVariable final Long id) {
		return ResponseEntity.ok().body(itemService.findItemWithDeleted(id));
	}

	@Login(admin = true)
	@GetMapping("/admin/items")
	public ResponseEntity<Page<ItemResponse>> findItemListWithDeleted(
		@LimitedSizePagination final Pageable pageable) {
		return ResponseEntity.ok().body(itemService.findItemListWithDeleted(pageable));
	}

	@Login(admin = true)
	@PostMapping("/items")
	public ResponseEntity<ItemResponse> createItem(@RequestBody @Valid final ItemCreateRequest itemCreateRequest) {
		return ResponseEntity.ok(itemService.createItem(itemCreateRequest));
	}

	@Login(admin = true)
	@PatchMapping("/items")
	public ResponseEntity<ItemResponse> updateItem(@RequestBody @Valid final ItemUpdateRequest itemUpdateRequest) {
		return ResponseEntity.ok(itemService.updateItem(itemUpdateRequest));
	}

	@Login(admin = true)
	@DeleteMapping("/items/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable final Long id) {
		itemService.deleteItem(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/item-years")
	public ResponseEntity<List<Integer>> findYears() {
		return ResponseEntity.ok(itemService.findItemYears());
	}
}
