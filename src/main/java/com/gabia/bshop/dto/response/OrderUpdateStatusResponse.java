package com.gabia.bshop.dto.response;

import com.gabia.bshop.entity.enumtype.OrderStatus;

public record OrderUpdateStatusResponse(
	Long orderId,
	Long memberId,
	OrderStatus status,
	long totalPrice
) {
}
