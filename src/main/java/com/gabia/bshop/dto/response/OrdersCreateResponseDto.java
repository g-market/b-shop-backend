package com.gabia.bshop.dto.response;

import java.util.List;

import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import lombok.Builder;

@Builder
public record OrdersCreateResponseDto(
	Long id,
	Long memberId,
	List<OrdersDto> orderItems,
	OrderStatus status,
	long totalPrice
) {
}
