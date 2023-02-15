package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ItemOptionChangeRequest(
	@NotNull(message = "옵션 ID는 필수입니다.")
	Long itemOptionId,
	String description,	
	@PositiveOrZero(message = "가격은 0원 이상입니다.")
	Integer optionPrice,
	@PositiveOrZero(message = "재고는 0개 이상 보유할 수 있습니다.")
	Integer stockQuantity

) {
}
