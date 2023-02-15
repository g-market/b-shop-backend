package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public interface ItemOptionMapper {
	ItemOptionMapper INSTANCE = Mappers.getMapper(ItemOptionMapper.class);

	@Mapping(target = "item", ignore = true)
	ItemOption ItemOptionDtoToEntity(ItemOptionDto itemOptionDto);

	ItemOptionDto ItemOptionToDto(ItemOption itemOption);

	@Mappings({
		@Mapping(source = "item.id", target = "itemId"),
		@Mapping(source = "id", target = "OptionId"),
	})
	ItemOptionResponse ItemOptionToResponse(ItemOption itemOption);

}
