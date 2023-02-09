package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

	ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Mappings({
		@Mapping(source = "categoryDto", target = "category"),
		@Mapping(source = "itemImageDtoList", target = "itemImageList"),
		@Mapping(source = "optionDtoList", target = "optionsList")
	})
	Item itemDtoToEntity(ItemRequest itemDto);

	@Mappings({
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "optionsList", target = "optionDtoList")
	})
	ItemResponse itemToItemResponse(Item item);

	@Mappings({
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "optionsList", target = "optionDtoList")
	})
	ItemRequest itemToItemRequest(Item item);
}
