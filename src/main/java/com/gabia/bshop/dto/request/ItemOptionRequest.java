package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ItemOptionRequest(
	@NotNull(message = "상품 옵션에 대한 설명을 입력하세요")
	String description,

	@PositiveOrZero(message = "optionPrice 는 0 이상입니다.")
	Integer optionPrice,
	@PositiveOrZero(message = "stockQuantity 는 0 이상입니다.")
	Integer stockQuantity
) {
}
