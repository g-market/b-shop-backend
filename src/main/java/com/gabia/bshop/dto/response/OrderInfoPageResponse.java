package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

import lombok.Builder;

@Builder
public record OrderInfoPageResponse(
	Long orderId,
	List<OrderItemDto> orderItemDtoList,
	String itemThumbnail,
	String itemName,
	int itemTotalCount,
	OrderStatus orderStatus,
	long totalPrice,
	LocalDateTime createdAt
) {
}
