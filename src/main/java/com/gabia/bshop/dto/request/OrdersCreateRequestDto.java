package com.gabia.bshop.dto.request;

import java.util.List;

import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import lombok.Builder;

@Builder
public record OrdersCreateRequestDto(
	Long memberId,
	List<OrdersDto> orderItems,
	OrderStatus status
) {
}
