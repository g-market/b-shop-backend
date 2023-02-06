package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.entity.enumtype.OrderStatus;

public record OrderInfoPageResponse(int resultCount, List<OrderInfo> orderInfos) {

	public record OrderInfo(long orderId,
							long itemId,
							String thumbnailImage,
							String representativeName,
							int itemTotalCount,
							OrderStatus orderStatus,
							long totalPrice,
							LocalDateTime createdAt) {

	}
}
