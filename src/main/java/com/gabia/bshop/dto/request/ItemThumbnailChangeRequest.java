package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemThumbnailChangeRequest(
	@NotNull(message = "imageId는 필수 값 입니다.")
	Long imageId
) {
}
