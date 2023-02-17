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
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderInfoMapper {
	OrderInfoMapper INSTANCE = Mappers.getMapper(OrderInfoMapper.class);

	default OrderInfoPageResponse orderInfoRelatedEntitiesToOrderInfoPageResponse(final List<Order> orderList,
		final List<OrderItem> orderItemList, final List<ItemImage> itemImagesWithItem) {

		// 주문 별 상품 종류 개수 수집
		final Map<Long, Integer> itemCountPerOrderId = orderItemList.stream()
			.collect(groupingBy(oi -> oi.getOrder().getId(), summingInt(OrderItem::getOrderCount)));

		final Map<Long, List<OrderItem>> orderItemsPerOrderId = orderList.stream()
			.collect(
				Collectors.toMap(o -> o.getId(), o -> o.getOrderItemList(), (p1, p2) -> p2));

		final Map<Long, ItemImage> itemImagePerItemId = itemImagesWithItem.stream()
			.collect(Collectors.toMap(i -> i.getItem().getId(), i -> i));

		return new OrderInfoPageResponse(orderList.size(),
			IntStream.range(0, orderList.size()).boxed()
				.map(i -> new OrderInfo(
					orderList.get(i).getId(),
					OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(
						orderItemsPerOrderId.get(orderList.get(i).getId())),
					//TODO: 썸네일 추가 필요
					//itemImagePerItemId.get(orderItemsPerOrderId.get(order.get(i).getId())).getItem().getThumbNail,
					"dummy",
					"dummy",
					itemCountPerOrderId.get(orderList.get(i).getId()),
					orderList.get(i).getStatus(),
					orderList.get(i).getTotalPrice(),
					orderList.get(i).getCreatedAt()))
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
				.map(i -> new OrderInfoSingleResponse.SingleOrder(orderItemsWithOrdersAndItem.get(i).getId(),
					orderItemsWithOrdersAndItem.get(i).getItem().getId(),
					orderItemsWithOrdersAndItem.get(i).getItem().getName(),
					orderItemsWithOrdersAndItem.get(i).getOrderCount(),
					orderItemsWithOrdersAndItem.get(i).getPrice(),
					thumbnailUrls.get(i)))
				.collect(Collectors.toList()));
	}
}
