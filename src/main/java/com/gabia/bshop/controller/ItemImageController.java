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

import com.gabia.bshop.dto.request.ItemImageCreateRequest;
import com.gabia.bshop.dto.request.ItemImageUpdateRequest;
import com.gabia.bshop.dto.request.ItemThumbnailUpdateRequest;
import com.gabia.bshop.dto.response.ItemImageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.service.ItemImageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemImageController {
	private final ItemImageService itemImageService;

	@GetMapping("/items/{itemId}/images/{imageId}")
	public ResponseEntity<ItemImageResponse> findItemImage(
		@PathVariable final Long itemId,
		@PathVariable final Long imageId) {
		return ResponseEntity.ok().body(itemImageService.findItemImage(itemId, imageId));
	}

	@GetMapping("/items/{itemId}/images")
	public ResponseEntity<List<ItemImageResponse>> findItemImageList(@PathVariable final Long itemId) {
		return ResponseEntity.ok().body(itemImageService.findItemImageList(itemId));
	}

	@Login(admin = true)
	@PostMapping("/items/{itemId}/images")
	public ResponseEntity<List<ItemImageResponse>> createItemImageList(
		@PathVariable final Long itemId,
		@RequestBody @Valid final ItemImageCreateRequest itemImageCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(itemImageService.createItemImage(itemId, itemImageCreateRequest));
	}

	@Login(admin = true)
	@PatchMapping("/items/{itemId}/images")
	public ResponseEntity<ItemImageResponse> updateItemImage(
		@PathVariable final Long itemId,
		@RequestBody @Valid final ItemImageUpdateRequest itemImageUpdateRequest) {
		return ResponseEntity.ok().body(itemImageService.updateItemImage(itemId, itemImageUpdateRequest));
	}

	@Login(admin = true)
	@PatchMapping("/items/{itemId}/thumbnail")
	public ResponseEntity<ItemResponse> updateItemThumbnail(
		@PathVariable final Long itemId,
		@RequestBody @Valid final ItemThumbnailUpdateRequest itemThumbnailUpdateRequest) {
		return ResponseEntity.ok().body(itemImageService.updateItemThumbnail(itemId, itemThumbnailUpdateRequest));
	}

	@Login(admin = true)
	@DeleteMapping("/items/{itemId}/images/{imageId}")
	public ResponseEntity<Void> deleteItemImage(
		@PathVariable final Long itemId,
		@PathVariable final Long imageId) {
		itemImageService.deleteItemImage(itemId, imageId);
		return ResponseEntity.noContent().build();
	}
}
