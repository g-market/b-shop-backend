package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public interface ItemOptionMapper {
	ItemOptionMapper INSTANCE = Mappers.getMapper(ItemOptionMapper.class);

	ItemOption ItemOptionDtoToEntity(ItemOptionDto itemOptionDto);

	ItemOptionDto ItemOptionToDto(ItemOption options);
}
