package com.gabia.bshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.ItemRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemImageResponse;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

	public static final ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Value("${minio.prefix}")
	private String MINIO_PREFIX;

	@Mappings({
		@Mapping(source = "category.id", target = "categoryId"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList")})
	public abstract ItemRequest itemToItemRequest(Item item);

	@Mappings({
		@Mapping(source = "category.id", target = "categoryId"),
		@Mapping(source = "item.id", target = "itemId")})
	public abstract ItemUpdateRequest itemToItemChangeRequest(Item item);

	@Named("itemToItemResponse")
	public ItemResponse itemToItemResponse(Item item) {
		if (item == null) {
			return null;
		}

		CategoryDto categoryDto = CategoryDto.builder()
			.id(item.getCategory().getId())
			.name(item.getCategory().getName())
			.build();

		List<ItemOptionResponse> itemOptionResponseList = item.getItemOptionList()
			.stream()
			.map(itemOption -> ItemOptionResponse.builder()
				.itemId(item.getId())
				.optionId(itemOption.getId())
				.optionPrice(itemOption.getOptionPrice())
				.stockQuantity(itemOption.getStockQuantity())
				.description(itemOption.getDescription())
				.build())
			.toList();

		List<ItemImageResponse> itemImageResponseList = item.getItemImageList()
			.stream()
			.map(itemImage -> ItemImageResponse.builder()
				.imageId(itemImage.getId())
				.imageUrl(MINIO_PREFIX + "/" + itemImage.getImageName())
				.build())
			.toList();

		ItemResponse itemResponse = ItemResponse.builder()
			.id(item.getId())
			.itemOptionResponseList(itemOptionResponseList)
			.itemImageResponseList(itemImageResponseList)
			.categoryDto(categoryDto)
			.name(item.getName())
			.itemStatus(item.getItemStatus())
			.basePrice(item.getBasePrice())
			.thumbnail(item.getThumbnail())
			.description(item.getDescription())
			.thumbnail(MINIO_PREFIX + "/" + item.getThumbnail())
			.year(item.getYear())
			.openAt(item.getOpenAt())
			.build();

		return itemResponse;
	}

	@Autowired
	public void setMinioPrefix(@Value("${minio.prefix}") String minioPrefix) {
		this.MINIO_PREFIX = minioPrefix;
	}
}
