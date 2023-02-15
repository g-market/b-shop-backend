package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemOptionChangeRequest(
	@NotNull(message = "옵션 ID는 필수입니다.")
	Long itemOptionId,
	String description,
	int optionPrice,
	int stockQuantity

) {
}
