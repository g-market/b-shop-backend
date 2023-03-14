package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemImageUpdateRequest(
	@NotNull(message = "imageId는 필수입니다.")
	Long imageId,

	@NotNull(message = "imageName은 필수입니다.")
	String imageName
) {
}
