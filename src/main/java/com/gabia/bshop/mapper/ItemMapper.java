package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

	ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Mappings({
		@Mapping(source = "categoryDto", target = "category"),
		@Mapping(source = "itemImageDtoList", target = "itemImageList"),
		@Mapping(source = "optionDtoList", target = "optionsList")
	})
	Item itemDtoToEntity(ItemDto itemDto);

	@Mappings({
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "optionsList", target = "optionDtoList")
	})
	ItemDto itemToDto(Item item);
}
