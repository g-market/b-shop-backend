package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.entity.enumtype.OrderStatus;

public record OrderInfoSingleResponse(long orderId,
									  int itemOrderTotalCount,
									  LocalDateTime orderDateTime,
									  OrderStatus orderStatus,
									  List<SingleOrder> orderItems) {

	public record SingleOrder(long orderItemId,
							  long itemId,
							  String itemName,
							  int orderCount,
							  long price,
							  String thumbnailImage) {
	}
}
