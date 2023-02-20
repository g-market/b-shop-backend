package com.gabia.bshop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {
	private final ImageService imageService;

	@PostMapping("/images")
	public ResponseEntity<List<ImageResponse>> uploadImage(
		@RequestParam("fileList") MultipartFile[] fileList) {
		return ResponseEntity.ok().body(imageService.uploadImage(fileList));
	}
}
