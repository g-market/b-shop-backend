package com.gabia.bshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.service.ItemOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ItemOptionController {

	private final ItemOptionService itemOptionService;

	@GetMapping("/items/{itemId}/options/{optionId}")
	public ResponseEntity<ItemOptionResponse> findItemOption(
		@PathVariable final Long itemId,
		@PathVariable final Long optionId) {
		return ResponseEntity.ok().body(itemOptionService.findItemOption(itemId, optionId));
	}

	@GetMapping("/items/{itemId}/options")
	public ResponseEntity<List<ItemOptionResponse>> findItemOptionList(@PathVariable final Long itemId) {
		return ResponseEntity.ok().body(itemOptionService.findOptionList(itemId));
	}

	@Login(admin = true)
	@PostMapping("/items/{itemId}/options")
	public ResponseEntity<ItemOptionResponse> createItemOption(
		@PathVariable final Long itemId,
		@RequestBody @Valid final ItemOptionRequest itemOptionRequest) {
		return ResponseEntity.ok().body(itemOptionService.createItemOption(itemId, itemOptionRequest));
	}

	@Login(admin = true)
	@PatchMapping("/items/{itemId}/options/{optionId}")
	public ResponseEntity<ItemOptionResponse> updateItemOption(
		@PathVariable final Long itemId,
		@PathVariable final Long optionId,
		@RequestBody @Valid final ItemOptionRequest itemOptionRequest) {
		return ResponseEntity.ok()
			.body(itemOptionService.changeItemOption(itemId, optionId, itemOptionRequest));
	}

	@Login(admin = true)
	@DeleteMapping("/items/{itemId}/options/{optionId}")
	public ResponseEntity<Void> deleteItemOption(
		@PathVariable final Long itemId,
		@PathVariable final Long optionId) {
		itemOptionService.deleteItemOption(itemId, optionId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
