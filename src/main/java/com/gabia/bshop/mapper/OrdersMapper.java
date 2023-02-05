package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    OrdersMapper INSTANCE = Mappers.getMapper(OrdersMapper.class);

    @Mappings({
            @Mapping(source = "memberId", target = "member.id"),
            @Mapping(target = "status", defaultValue = "PENDING"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "totalPrice", ignore = true)
    })
    Orders ordersCreateDtoToEntity(OrdersCreateRequestDto ordersCreateRequestDto);

    @Mapping(source = "item.id", target = "id")
    OrdersDto orderItemToOrdersDto(OrderItem orderItem);

    List<OrdersDto> orderItemListToOrderDtoList(List<OrderItem> orderItemList);

    default OrdersCreateResponseDto orderCreateEntityToOrdersCreateResponseDto(Orders orders,
            List<OrderItem> orderItemList) {

        return OrdersCreateResponseDto.builder()
                .itemList(orderItemListToOrderDtoList(orderItemList))
                .memberId(orders.getMember().getId())
                .status(orders.getStatus())
                .totalPrice(orders.getTotalPrice())
                .build();
    }
}
