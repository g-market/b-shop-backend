package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemThumbnailChangeRequest(
	@NotNull(message = "itemId는 필수 값입니다.")
	Long itemId,
	@NotNull(message = "imageId는 필수 값 입니다.")
	Long imageId
) {
}
