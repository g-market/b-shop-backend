package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

	ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Mapping(source = "categoryDto", target = "category")
	Item itemDtoToEntity(ItemDto itemDto);

	@Mapping(source = "category", target = "categoryDto")
	ItemDto itemToDto(Item item);
}
