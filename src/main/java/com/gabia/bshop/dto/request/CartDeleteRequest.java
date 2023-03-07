package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartDeleteRequest(
	@NotNull(message = "아이템 ID는 필수값입니다.")
	Long itemId,

	@NotNull(message = "옵션 ID는 필수값입니다.")
	Long itemOptionId
) {
}
