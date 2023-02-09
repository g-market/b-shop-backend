package com.gabia.bshop.dto.request;

import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import lombok.Builder;

@Builder
public record OrderCreateRequestDto(
	Long memberId,
	List<OrderItemDto> orderItemDtoList,
	OrderStatus status
) {
}
