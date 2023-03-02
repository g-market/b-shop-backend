package com.gabia.bshop.dto.response;

import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import lombok.Builder;

@Builder
public record OrderCreateResponse(
	Long id,
	Long memberId,
	List<OrderItemDto> orderItemDtoList,
	OrderStatus status,
	long totalPrice
) {
}
