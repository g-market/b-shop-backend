package com.gabia.bshop.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;

public record ItemImageUpdateRequest(
	@NotNull(message = "imageId는 필수 입니다.")
	Long imageId,
	@URL(message = "imageUrl이 URL 형식에 맞지 않습니다.")
	String imageUrl
) {
}
