package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ItemOptionChangeRequest(
	@NotNull(message = "itemOptionId 는 필수입니다.")
	Long itemOptionId,
	String description,

	@PositiveOrZero(message = "optionPrice 0 이상입니다.")
	Integer optionPrice,

	@PositiveOrZero(message = "stockQuantity 0 이상입니다.")
	Integer stockQuantity
) {
}
