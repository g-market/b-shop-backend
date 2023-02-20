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

	@GetMapping("/items/{itemId}/options/{optionId}")
	public ResponseEntity<ItemOptionResponse> findItemOption(
		@PathVariable("itemId") final Long itemId,
		@PathVariable("optionId") final Long optionId) {
		return ResponseEntity.ok().body(itemOptionService.findItemOption(itemId, optionId));
	}

	@GetMapping("/items/{itemId}/options")
	public ResponseEntity<List<ItemOptionResponse>> findItemOptionList(@PathVariable final Long itemId) {
		return ResponseEntity.ok().body(itemOptionService.findOptionList(itemId));
	}

	@Login(admin = true)
	@PostMapping("/items/{itemId}/options")
	public ResponseEntity<ItemOptionResponse> creatItemOption(
		@PathVariable("itemId") final Long itemId,
		@RequestBody @Valid final ItemOptionRequest itemOptionRequest) {
		return ResponseEntity.ok().body(itemOptionService.createItemOption(itemId, itemOptionRequest));
	}

	@Login(admin = true)
	@PatchMapping("/item/{itemId}/options/{OptionId}")
	public ResponseEntity<ItemOptionResponse> updateItemOption(
		@PathVariable("itemId") final Long itemId,
		@PathVariable("OptionId") final Long itemOptionId,
		@RequestBody @Valid final ItemOptionChangeRequest itemOptionChangeRequest) {
		return ResponseEntity.ok()
			.body(itemOptionService.changeItemOption(itemId, itemOptionId, itemOptionChangeRequest));
	}

	@Login(admin = true)
	@DeleteMapping("/items/{itemId}/options/{optionId}")
	public ResponseEntity<Void> deleteItemOption(
		@PathVariable("itemId") final Long itemId,
		@PathVariable("OptionId") final Long itemOptionId) {
		itemOptionService.deleteItemOption(itemId, itemOptionId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
