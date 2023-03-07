package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartCreateRequest(
	@NotNull(message = "아이템 ID는 필수값입니다.")
	Long itemId,

	@NotNull(message = "옵션 ID는 필수값입니다.")
	Long itemOptionId,

	@NotNull(message = "주문량은 필수값입니다.")
	@Positive(message = "주문량은 양수만 허용합니다.")
	int orderCount
) {
}
