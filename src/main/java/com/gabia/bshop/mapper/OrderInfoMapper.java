package com.gabia.bshop.mapper;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoPageResponse.OrderInfo;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse.SingleOrder;
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
public interface OrderInfoMapper {
    OrderInfoMapper INSTANCE = Mappers.getMapper(OrderInfoMapper.class);

    default OrderInfoPageResponse orderInfoRelatedEntitiesToOrderInfoPageResponse(final List<Orders> orders, final List<OrderItem> orderItems, final List<ItemImage> itemImagesWithItem) {
        // 주문 별 상품 종류 개수 수집
        final Map<Long, Integer> itemCountPerOrder = orderItems.stream()
                .collect(groupingBy(oi -> oi.getOrder().getId(), summingInt(OrderItem::getOrderCount)));

        return new OrderInfoPageResponse(orders.size(),
                IntStream.range(0, orders.size())
                        .boxed()
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

    default OrderInfoSingleResponse orderInfoSingleDTOResponse(final List<OrderItem> orderItemsWithOrdersAndItem, final List<String> thumbnailUrls) {
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
