package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ItemOptionRequest(
	@NotNull(message = "itemId 는 필수입니다.")
	Long itemId,

	String description,

	@PositiveOrZero(message = "optionPrice 는 0 이상입니다.")
	Integer optionPrice,
	@PositiveOrZero(message = "stockQuantity 는 0 이상입니다.")
	Integer stockQuantity
) {
}