package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.request.OrderCreateRequest;
import com.gabia.bshop.dto.response.OrderCreateResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
import com.gabia.bshop.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

	@Mappings({
		@Mapping(source = "memberId", target = "member.id"),
		@Mapping(source = "orderCreateRequest.orderItemDtoList", target = "orderItemList"),
		@Mapping(source = "orderCreateRequest.status", target = "status", defaultValue = "ACCEPTED"),
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "totalPrice", ignore = true),
	})
	Order orderCreateRequsetToEntity(Long memberId, OrderCreateRequest orderCreateRequest);

	@Mappings({
		@Mapping(source = "member.id", target = "memberId"),
		@Mapping(source = "orderItemList", target = "orderItemDtoList")
	})
	OrderCreateResponse orderCreateResponseToDto(Order order);

	//orderStatus Update
	@Mapping(source = "id", target = "orderId")
	@Mapping(source = "member.id", target = "memberId")
	OrderUpdateStatusResponse orderToOrderUpdateStatusResponse(Order order);
}
