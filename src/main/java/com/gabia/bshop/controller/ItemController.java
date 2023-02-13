package com.gabia.bshop.controller;

import java.util.List;

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

import com.gabia.bshop.dto.request.ItemChangeRequest;
import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;

	@GetMapping("/items/{id}")
	public ResponseEntity<ItemResponse> findItem(@PathVariable("id") Long id) {
		return ResponseEntity.ok().body(itemService.findItem(id));
	}

	@GetMapping("/items")
	public ResponseEntity<List<ItemResponse>> pageItem(Pageable pageable) {
		return ResponseEntity.ok().body(itemService.findItemList(pageable));
	}

	@PostMapping("/items")
	public ResponseEntity<ItemResponse> creatItem(@RequestBody ItemRequest itemRequest) {
		return ResponseEntity.ok().body(itemService.createItem(itemRequest));
	}

	@PatchMapping("/items/{id}")
	public ResponseEntity<ItemResponse> updateItem(@RequestBody ItemChangeRequest itemChangeRequest,
		@PathVariable Long id) {
		return ResponseEntity.ok().body(itemService.updateItem(id, itemChangeRequest));
	}

	@DeleteMapping("/items/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
		itemService.deleteItem(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
