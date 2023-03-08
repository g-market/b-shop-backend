package com.gabia.bshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ItemImageDto(
	@NotNull(message = "이미지 ID는 필수 입니다.")
	Long imageId,
	@NotNull(message = "url 은 필수 입니다.")
	String url) {
}
