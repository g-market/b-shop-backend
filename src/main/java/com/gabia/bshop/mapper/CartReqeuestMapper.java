package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.request.CartCreateRequest;
import com.gabia.bshop.dto.request.CartDeleteRequest;

@Mapper(componentModel = "spring")
public interface CartReqeuestMapper {

	CartReqeuestMapper INSTANCE = Mappers.getMapper(CartReqeuestMapper.class);

	@Mappings({
		@Mapping(source = "itemId", target = "itemId"),
		@Mapping(source = "itemOptionId", target = "itemOptionId"),
		@Mapping(source = "orderCount", target = "orderCount")
	})
	CartDto toCartDto(final CartCreateRequest cartCreateRequest);

	@Mappings({
		@Mapping(source = "itemId", target = "itemId"),
		@Mapping(source = "itemOptionId", target = "itemOptionId"),
		@Mapping(target = "orderCount", ignore = true)
	})
	CartDto toCartDto(final CartDeleteRequest cartDeleteRequest);
}
