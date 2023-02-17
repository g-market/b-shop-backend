package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.enumtype.OrderStatus;

public record OrderInfoPageResponse(
	int resultCount,
	List<OrderInfo> orderInfos) {

	public record OrderInfo(
		long orderId,
		//List<Long> itemId,	// TODO: Order 조회 추후 리팩토링 진행
		List<OrderItemDto> orderItemDtoList,
		String thumbnailImage,
		String representativeName,
		int itemTotalCount,
		OrderStatus orderStatus,
		long totalPrice,
		LocalDateTime createdAt) {
	}
}
