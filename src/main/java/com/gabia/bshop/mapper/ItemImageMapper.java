package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.ItemImageResponse;
import com.gabia.bshop.entity.ItemImage;

@Mapper(componentModel = "spring")
public abstract class ItemImageMapper extends MapperSupporter {

	public static final ItemImageMapper INSTANCE = Mappers.getMapper(ItemImageMapper.class);

	@Mappings({
		@Mapping(source = "id", target = "imageId"),
		@Mapping(target = "imageUrl", expression = "java(addPrefixToImageUrl(itemImage))")
	})
	public abstract ItemImageResponse itemImageToItemImageResponse(ItemImage itemImage);
}
