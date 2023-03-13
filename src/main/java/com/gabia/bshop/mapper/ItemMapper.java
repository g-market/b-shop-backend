package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.request.ItemCreateRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemAllInfoResponse;
import com.gabia.bshop.dto.response.ItemPageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public abstract class ItemMapper extends MapperSupporter {

	public static final ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList"),
		@Mapping(target = "itemImageDtoList", expression = "java(addPrefixToImageName(item))"),
		@Mapping(target = "thumbnail", expression = "java(addPrefixToThumbnail(item))")
	})
	public abstract ItemAllInfoResponse itemToItemAllInfoResponse(Item item);

	@Mappings({
		@Mapping(source = "category.id", target = "categoryId"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList")
	})
	public abstract ItemCreateRequest itemToItemCreateRequest(Item item);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category.id", target = "categoryId")
	})
	public abstract ItemUpdateRequest itemToItemUpdateRequest(Item item);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(target = "thumbnail", expression = "java(addPrefixToThumbnail(item))")
	})
	public abstract ItemPageResponse itemToItemPageResponse(Item item);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList"),
		@Mapping(target = "itemImageDtoList", expression = "java(addPrefixToImageName(item))"),
		@Mapping(target = "thumbnail", expression = "java(addPrefixToThumbnail(item))")
	})
	public abstract ItemResponse itemToItemResponse(Item item);

}
