package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemThumbnailUpdateRequest(
	@NotNull(message = "imageId는 필수 값 입니다.")
	Long imageId
) {
}
