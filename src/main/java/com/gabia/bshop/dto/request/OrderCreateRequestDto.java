package com.gabia.bshop.dto.request;

import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderCreateRequestDto(
	@Valid
	@NotNull(message = "1개 이상의 아이템을 주문해주세요.")
	@Size(min = 1, message = "1개 이상의 아이템을 주문해주세요.")
	List<OrderItemDto> orderItemDtoList,
	OrderStatus status
) {
}
