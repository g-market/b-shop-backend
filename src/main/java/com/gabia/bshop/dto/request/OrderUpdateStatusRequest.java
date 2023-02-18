package com.gabia.bshop.dto.request;

import com.gabia.bshop.entity.enumtype.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record OrderUpdateStatusRequest(
	@NotNull(message = "주문 ID는 필수값입니다.")
	Long orderId,
	@NotNull(message = "주문 상태는 필수값입니다.")
	OrderStatus status
) {
}
