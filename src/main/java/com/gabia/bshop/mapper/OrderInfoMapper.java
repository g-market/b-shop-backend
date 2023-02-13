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
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;

@Mapper(componentModel = "spring")
public interface OrderInfoMapper {
	OrderInfoMapper INSTANCE = Mappers.getMapper(OrderInfoMapper.class);

	default OrderInfoPageResponse orderInfoRelatedEntitiesToOrderInfoPageResponse(final List<Orders> orders,
		final List<OrderItem> orderItemList, final List<ItemImage> itemImagesWithItem) {

		// 주문 별 상품 종류 개수 수집
		final Map<Long, Integer> itemCountPerOrder = orderItemList.stream()
			.collect(groupingBy(oi -> oi.getOrder().getId(), summingInt(OrderItem::getOrderCount)));

		return new OrderInfoPageResponse(orders.size(),
			IntStream.range(0, orders.size()).boxed()
				.map(i -> new OrderInfo(
					orders.get(i).getId(),
					itemImagesWithItem.get(i).getUrl(),
					itemImagesWithItem.get(i).getItem().getName(),
					itemCountPerOrder.get(orders.get(i).getId()),
					orders.get(i).getStatus(),
					orders.get(i).getTotalPrice(),
					orders.get(i).getCreatedAt()))
				.collect(Collectors.toList()));
	}
}
