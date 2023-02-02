package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.entity.Orders;
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
}
