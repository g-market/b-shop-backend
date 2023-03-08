package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.entity.enumtype.OrderStatus;

import lombok.Builder;

@Builder
public record OrderInfoResponse(
	Long orderId,
	long totalPrice,
	LocalDateTime createdAt,
	OrderStatus orderStatus,
	List<SingleOrder> orderItemList
) {

	@Builder
	public record SingleOrder(
		long orderItemId,
		Long itemId,
		Long itemOptionId,
		String itemName,
		String itemOptionDescription,
		int orderCount,
		long price,
		String itemThumbnail
	) {

	}
}
