package com.gabia.bshop.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.Member;

public class MapperSupporter {

	private static String MINIO_PREFIX;

	@Autowired
	public void setMinioPrefix(@Value("${minio.prefix}") String minioPrefix) {
		MINIO_PREFIX = minioPrefix;
	}

	public List<ItemImageDto> addPrefixToImageName(Item item) {
		if (item.getItemImageList() == null) {
			return null;
		}
		return item.getItemImageList()
			.stream()
			.map(itemImage -> ItemImageDto.builder()
				.imageId(itemImage.getId())
				.imageUrl(addPrefixToString(itemImage.getImageName()))
				.build())
			.toList();
	}

	public String addPrefixToThumbnail(Item item) {
		if (item == null) {
			return null;
		}
		return addPrefixToString(item.getThumbnail());
	}

	public String addPrefixToImageUrl(ItemImage itemImage) {
		if (itemImage == null) {
			return null;
		}
		return addPrefixToString(itemImage.getImageName());
	}

	public String addPrefixToProfileUrl(Member member) {
		if (member == null) {
			return null;
		}
		return addPrefixToString(member.getProfileImageUrl());
	}

	private String addPrefixToString(String string) {
		return MINIO_PREFIX + "/" + string;
	}

}
