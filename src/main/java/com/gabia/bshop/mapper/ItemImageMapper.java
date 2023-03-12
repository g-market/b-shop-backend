package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gabia.bshop.dto.response.ItemImageResponse;
import com.gabia.bshop.entity.ItemImage;

@Mapper(componentModel = "spring")
public abstract class ItemImageMapper {

	public static final ItemImageMapper INSTANCE = Mappers.getMapper(ItemImageMapper.class);

	@Value("${minio.prefix}")
	private String MINIO_PREFIX;

	@Named("itemImageToItemImageResponse")
	public ItemImageResponse itemImageToItemImageResponse(ItemImage itemImage) {
		if (itemImage == null) {
			return null;
		}

		ItemImageResponse itemImageResponse = ItemImageResponse.builder()
			.imageId(itemImage.getId())
			.imageUrl(MINIO_PREFIX + "/" + itemImage.getImageName())
			.build();

		return itemImageResponse;
	}

	@Autowired
	public void setMinioPrefix(@Value("${minio.prefix}") String minioPrefix) {
		this.MINIO_PREFIX = minioPrefix;
	}
}
