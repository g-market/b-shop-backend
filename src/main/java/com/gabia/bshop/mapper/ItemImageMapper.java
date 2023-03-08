package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.entity.ItemImage;

@Mapper(componentModel = "spring")
public interface ItemImageMapper {
	ItemImageMapper INSTANCE = Mappers.getMapper(ItemImageMapper.class);

	@Mapping(source = "id", target = "imageId")
	ItemImageDto itemImageToDto(ItemImage itemImage);
}
