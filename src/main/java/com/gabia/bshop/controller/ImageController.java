package com.gabia.bshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.bshop.dto.response.ImageResponse;
import com.gabia.bshop.dto.validator.ValidFile;
import com.gabia.bshop.dto.validator.ValidFileLength;
import com.gabia.bshop.dto.validator.group.FileValidationGroups;
import com.gabia.bshop.dto.validator.group.ValidationSequence;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated(ValidationSequence.class)
public class ImageController {

	private final ImageService imageService;

	@Login
	@PostMapping("/images")
	public ResponseEntity<List<ImageResponse>> uploadImage(
		@RequestParam("fileList")
		@ValidFile(groups = FileValidationGroups.NotSuppoertedFileGroup.class)
		@ValidFileLength(groups = FileValidationGroups.NotEmptyFileListGroup.class) final MultipartFile[] fileList) {
		return ResponseEntity.status(HttpStatus.CREATED).body(imageService.uploadImage(fileList));
	}
}
