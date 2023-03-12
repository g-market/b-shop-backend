package com.gabia.bshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ItemImageDto(
	@NotNull(message = "imageId는 필수 입니다.")
	Long imageId,
	@NotNull(message = "imageUrl은 필수 입니다.")
	String imageUrl) {
}
