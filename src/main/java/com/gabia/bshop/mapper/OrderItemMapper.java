package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.entity.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    //@Mapping(target = "id", expression = "java(ordersCreateRequestDto.itemDtoList().stream().map(OrderItemDto::id))")
    //@Mapping(source = "itemDtoList.id", target = "id")
    OrderItem orderItemDtoToEntity(OrderItemDto orderItemDto);

    OrderItemDto orderItemDto(OrderItem orderItem);

    List<OrderItemDto> toDtoList(List<OrderItem> entityList);
}
