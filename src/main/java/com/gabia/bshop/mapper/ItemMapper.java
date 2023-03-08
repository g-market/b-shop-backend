package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.request.ItemCreateRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemPageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

	ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Mappings({
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList"),
	})
	ItemResponse itemToItemResponse(Item item);

	@Mappings({
		@Mapping(source = "category.id", target = "categoryId"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList")
	})
	ItemCreateRequest itemToItemCreateRequest(Item item);

	@Mappings({
		@Mapping(source = "category.id", target = "categoryId"),
		@Mapping(source = "id", target = "itemId"),
	})
	ItemUpdateRequest itemToItemUpdateRequest(Item item);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category", target = "categoryDto"),
	})
	ItemPageResponse itemToItemPageResponse(Item item);
}
