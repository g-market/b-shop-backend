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
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.dto.request.ItemCreateRequest;
import com.gabia.bshop.dto.request.ItemUpdateRequest;
import com.gabia.bshop.dto.response.ItemAllInfoResponse;
import com.gabia.bshop.dto.response.ItemPageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

	public static final ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

	@Value("${minio.prefix}")
	private String MINIO_PREFIX;

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category", target = "categoryDto"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList"),
	})
	ItemAllInfoResponse itemToItemAllInfoResponse(Item item);

	@Mappings({
		@Mapping(source = "category.id", target = "categoryId"),
		@Mapping(source = "itemImageList", target = "itemImageDtoList"),
		@Mapping(source = "itemOptionList", target = "itemOptionDtoList")
	})
	public abstract ItemCreateRequest itemToItemCreateRequest(Item item);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category.id", target = "categoryId"),
	})
	public abstract ItemUpdateRequest itemToItemUpdateRequest(Item item);

	@Mappings({
		@Mapping(source = "id", target = "itemId"),
		@Mapping(source = "category", target = "categoryDto"),
	})
	public abstract ItemPageResponse itemToItemPageResponse(Item item);

	@Named("itemToItemResponse")
	public ItemResponse itemToItemResponse(Item item) {
		if (item == null) {
			return null;
		}

		CategoryDto categoryDto = CategoryDto.builder()
			.id(item.getCategory().getId())
			.name(item.getCategory().getName())
			.build();

		List<ItemOptionDto> itemOptionDtoList = item.getItemOptionList()
			.stream()
			.map(itemOption -> ItemOptionDto.builder()
				.id(itemOption.getId())
				.optionPrice(itemOption.getOptionPrice())
				.stockQuantity(itemOption.getStockQuantity())
				.description(itemOption.getDescription())
				.build())
			.toList();

		List<ItemImageDto> itemImageDtoList = item.getItemImageList()
			.stream()
			.map(itemImage -> ItemImageDto.builder()
				.imageId(itemImage.getId())
				.imageUrl(MINIO_PREFIX + "/" + itemImage.getImageName())
				.build())
			.toList();

		ItemResponse itemResponse = ItemResponse.builder()
			.itemId(item.getId())
			.itemOptionDtoList(itemOptionDtoList)
			.itemImageDtoList(itemImageDtoList)
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
