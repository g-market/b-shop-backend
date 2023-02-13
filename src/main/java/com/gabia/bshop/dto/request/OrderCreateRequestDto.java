package com.gabia.bshop.dto.request;

import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record OrderCreateRequestDto(
	Long memberId,
	@Valid
	List<OrderItemDto> orderItemDtoList,
	OrderStatus status
) {
}
