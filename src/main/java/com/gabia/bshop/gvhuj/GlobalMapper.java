package com.gabia.bshop.gvhuj;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoPageResponse.OrderInfo;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GlobalMapper {
    GlobalMapper INSTANCE = Mappers.getMapper(GlobalMapper.class);

    default OrderInfoPageResponse orderInfoRelatedEntitiesToOrderInfoPageResponse(List<Orders> orders, List<OrderItem> orderItems, List<ItemImage> itemImagesWithItem) {

        // 주문 별 상품 종류 개수 수집
        Map<Long, Integer> itemCountPerOrder = orderItems.stream()
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
