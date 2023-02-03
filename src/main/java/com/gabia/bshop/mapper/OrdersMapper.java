package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.OrderItemDto;
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

    Orders ordersDtoToEntity(OrdersDto ordersDto);

    @Mappings({
            @Mapping(source = "member", target = "memberDto"),
            //@Mapping(source = "product", target = "productResponse")
            })
    OrdersDto ordersToDto(Orders orders);

    //OrderCreate
    Orders ordersCreateDtoToEntity(OrdersCreateRequestDto ordersCreateRequestDto);

    @Mappings({
        //@Mapping(target = "memberId", expression = "java(orders.getMember().getId())")
        @Mapping(source = "orders.member", target = "memberDto"),
        @Mapping(source = "orderItemList", target = "itemDtoList")
    })
    OrdersCreateResponseDto ordersToCreateDto(Orders orders, List<OrderItem> orderItemList);
}
