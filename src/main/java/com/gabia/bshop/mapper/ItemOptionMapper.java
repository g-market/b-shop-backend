package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public interface ItemOptionMapper {
	ItemOptionMapper INSTANCE = Mappers.getMapper(ItemOptionMapper.class);

	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "item", ignore = true)
	})
	ItemOption itemOptionRequestToEntity(ItemOptionRequest itemOptionRequest);

	@Mappings({
		@Mapping(source = "item.id", target = "itemId"),
		@Mapping(source = "id", target = "optionId"),
	})
	ItemOptionResponse itemOptionToResponse(ItemOption itemOption);

}
