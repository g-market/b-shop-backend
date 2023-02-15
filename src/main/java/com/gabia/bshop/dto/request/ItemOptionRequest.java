package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ItemOptionRequest (
	@NotNull(message = "아이템 ID 는 필수입니다.")
	Long itemId,

	String description,

	@PositiveOrZero(message = "가격은 0원 이상입니다.")
	int optionPrice,
	@PositiveOrZero(message = "재고는 0개 이상 보유할 수 있습니다.")
	int stockQuantity
){
}
