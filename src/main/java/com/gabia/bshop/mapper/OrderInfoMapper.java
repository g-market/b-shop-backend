package com.gabia.bshop.mapper;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoPageResponse.OrderInfo;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse.SingleOrder;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;

@Mapper(componentModel = "spring")
public interface OrderInfoMapper {
	OrderInfoMapper INSTANCE = Mappers.getMapper(OrderInfoMapper.class);

	default OrderInfoPageResponse orderInfoRelatedEntitiesToOrderInfoPageResponse(final List<Orders> orders,
		final List<OrderItem> orderItems, final List<ItemImage> itemImagesWithItem) {
		final Map<Long, Integer> itemCountPerOrderId = orderItems.stream()
			.collect(groupingBy(oi -> oi.getOrder().getId(), summingInt(OrderItem::getOrderCount)));
		final Map<Long, OrderItem> orderItemsPerOrderId = orderItems.stream()
			.collect(Collectors.toMap(oi -> oi.getOrder().getId(), oi -> oi, (p1, p2) -> p2));
		final Map<Long, ItemImage> itemImagePerItemId = itemImagesWithItem.stream()
			.collect(Collectors.toMap(i -> i.getItem().getId(), i -> i));

		return new OrderInfoPageResponse(orders.size(),
			IntStream.range(0, orders.size())
				.boxed()
				.map(i -> new OrderInfo(
					orders.get(i).getId(),
					orderItemsPerOrderId.get(orders.get(i).getId()).getItem().getId(),
					itemImagePerItemId.get(orderItemsPerOrderId.get(orders.get(i).getId()).getItem().getId()).getUrl(),
					itemImagePerItemId.get(orderItemsPerOrderId.get(orders.get(i).getId()).getItem().getId())
						.getItem()
						.getName(),
					itemCountPerOrderId.get(orders.get(i).getId()),
					orders.get(i).getStatus(),
					orders.get(i).getTotalPrice(),
					orders.get(i).getCreatedAt()))
				.collect(Collectors.toList()));
	}

	default OrderInfoSingleResponse orderInfoSingleDTOResponse(final List<OrderItem> orderItemsWithOrdersAndItem,
		final List<String> thumbnailUrls) {
		if (orderItemsWithOrdersAndItem == null) {
			return null;
		}
		return new OrderInfoSingleResponse(orderItemsWithOrdersAndItem.get(0).getOrder().getId(),
			orderItemsWithOrdersAndItem.size(),
			orderItemsWithOrdersAndItem.get(0).getOrder().getCreatedAt(),
			orderItemsWithOrdersAndItem.get(0).getOrder().getStatus(),
			IntStream.range(0, orderItemsWithOrdersAndItem.size())
				.boxed()
				.map(i -> new SingleOrder(orderItemsWithOrdersAndItem.get(i).getId(),
					orderItemsWithOrdersAndItem.get(i).getItem().getId(),
					orderItemsWithOrdersAndItem.get(i).getItem().getName(),
					orderItemsWithOrdersAndItem.get(i).getOrderCount(),
					orderItemsWithOrdersAndItem.get(i).getPrice(),
					thumbnailUrls.get(i)))
				.collect(Collectors.toList()));
	}
}
