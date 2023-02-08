package com.gabia.bshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.dto.request.OrdersCreateRequestDto;
import com.gabia.bshop.dto.response.OrdersCreateResponseDto;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

	OrdersMapper INSTANCE = Mappers.getMapper(OrdersMapper.class);

	@Mappings({
		@Mapping(source = "memberId", target = "member.id"),
		@Mapping(source = "status", target = "status", defaultValue = "ACCEPTED"),
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "totalPrice", ignore = true),
	})
	Orders ordersCreateDtoToEntity(OrdersCreateRequestDto ordersCreateRequestDto);

	@Mapping(source = "member.id", target = "memberId")
	OrdersCreateResponseDto ordersCreateResponseDto(Orders orders);

	@Mappings({
		@Mapping(source = "id", target = "item.id"),
		@Mapping(source = "optionId", target = "option.id"),
		@Mapping(target = "order", ignore = true),
		@Mapping(target = "price", ignore = true),
	})
	OrderItem orderDtoToOrderItem(OrdersDto ordersDto);

	@Mapping(source = "item.id", target = "id")
	@Mapping(source = "option.id", target = "optionId")
	OrdersDto orderItemToOrdersDto(OrderItem orderItem);

	List<OrderItem> orderDtoListToOrderItemList(List<OrdersDto> orderDtoList);

	List<OrdersDto> orderItemListToOrderDtoList(List<OrderItem> orderItemList);
}
