package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemImageUpdateRequest(
	@NotNull(message = "imageId는 필수 입니다.")
	Long imageId,
	@NotNull(message = "imageUrl은 필수 입니다.")
	String imageUrl
) {
}
