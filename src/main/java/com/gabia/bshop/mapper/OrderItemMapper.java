package com.gabia.bshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

	OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

	@Mappings({
		@Mapping(source = "itemId", target = "item.id"),
		@Mapping(source = "itemOptionId", target = "option.id"),
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "order", ignore = true),
		@Mapping(target = "price", ignore = true),
	})
	OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto);

	@Mapping(source = "item.id", target = "itemId")
	@Mapping(source = "option.id", target = "itemOptionId")
	OrderItemDto orderItemToOrdersDto(OrderItem orderItem);

	List<OrderItemDto> orderItemListToOrderItemDtoList(List<OrderItem> orderItemList);
}
