package com.gabia.bshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

	@Mappings({
		@Mapping(source = "memberId", target = "member.id"),
		@Mapping(source = "orderItemDtoList", target = "orderItems"),
		@Mapping(source = "status", target = "status", defaultValue = "ACCEPTED"),
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "totalPrice", ignore = true),
	})
	Order ordersCreateDtoToEntity(OrderCreateRequestDto orderCreateRequestDto);

	@Mappings({
		@Mapping(source = "member.id", target = "memberId"),
		@Mapping(source = "orderItems", target = "orderItemDtoList")
	})
	OrderCreateResponseDto ordersCreateResponseDto(Order order);

	@Mappings({
		@Mapping(source = "id", target = "item.id"),
		@Mapping(source = "optionId", target = "option.id"),
		@Mapping(target = "order", ignore = true),
		@Mapping(target = "price", ignore = true),
	})
	OrderItem orderDtoToOrderItem(OrderItemDto orderDto);

	@Mapping(source = "item.id", target = "id")
	@Mapping(source = "option.id", target = "optionId")
	OrderItemDto orderItemToOrdersDto(OrderItem orderItem);

	List<OrderItem> orderDtoListToOrderItemList(List<OrderItemDto> orderDtoList);

	List<OrderItemDto> orderItemListToOrderItemDtoList(List<OrderItem> orderItemList);
}
