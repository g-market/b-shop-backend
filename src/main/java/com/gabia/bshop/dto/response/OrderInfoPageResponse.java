package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

public record OrderInfoPageResponse(
	int resultCount,
	List<OrderInfo> orderInfoList) {

	public record OrderInfo(
		Long orderId,
		List<OrderItemDto> orderItemDtoList,
		String thumbnailImage,
		String representativeName,
		int itemTotalCount,
		OrderStatus orderStatus,
		long totalPrice,
		LocalDateTime createdAt) {
	}
}
