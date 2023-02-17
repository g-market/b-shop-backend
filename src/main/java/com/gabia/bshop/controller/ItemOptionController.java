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

import com.gabia.bshop.dto.request.ItemOptionChangeRequest;
import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.service.ItemOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemOptionController {

	private final ItemOptionService itemOptionService;

	@GetMapping("/items/options/{optionId}")
	public ResponseEntity<ItemOptionResponse> findItemOption(@PathVariable final Long optionId) {
		return ResponseEntity.ok().body(itemOptionService.findItemOption(optionId));
	}

	@GetMapping("/items/{itemId}/options")
	public ResponseEntity<List<ItemOptionResponse>> pageItemOption(@PathVariable final Long itemId) {
		return ResponseEntity.ok().body(itemOptionService.findOptionList(itemId));
	}
	@Login(admin = true)
	@PostMapping("/items/options")
	public ResponseEntity<ItemOptionResponse> creatItemOption(
		@RequestBody @Valid final ItemOptionRequest itemOptionRequest) {
		return ResponseEntity.ok().body(itemOptionService.createItemOption(itemOptionRequest));
	}
	@Login(admin = true)
	@PatchMapping("/items/options")
	public ResponseEntity<ItemOptionResponse> updateItemOption(
		@RequestBody @Valid final ItemOptionChangeRequest itemOptionChangeRequest) {
		return ResponseEntity.ok().body(itemOptionService.changeItemOption(itemOptionChangeRequest));
	}
	@Login(admin = true)
	@DeleteMapping("/items/options/{optionId}")
	public ResponseEntity<Void> deleteItemOption(@PathVariable final Long optionId) {
		itemOptionService.deleteItemOption(optionId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}