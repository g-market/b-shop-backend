package com.gabia.bshop.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.request.CartCreateRequest;
import com.gabia.bshop.dto.request.CartDeleteRequest;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public abstract class CartMapper extends MapperSupporter {

	public static final CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

	String DELIMITER = "-";

	public abstract CartDto cartCreateRequestToCartDto(final CartCreateRequest cartCreateRequest);

	@Mappings({
		@Mapping(target = "orderCount", ignore = true)
	})
	public abstract CartDto cartDeleteRequestToCartDto(final CartDeleteRequest cartDeleteRequest);

	@Named("itemOptionListAndCartDtoToCartResponse")
	public List<CartResponse> itemOptionListAndCartDtoToCartResponse(final List<ItemOption> itemOptionList,
		final List<CartDto> cartDtoList) {
		final List<CartResponse> cartResponseList = new ArrayList<>(itemOptionList.size());
		final HashMap<String, Integer> map = new HashMap<>(cartDtoList.size());
		for (CartDto cartDto : cartDtoList) {
			final String key = cartDto.itemId() + DELIMITER + cartDto.itemOptionId();
			map.put(key, cartDto.orderCount());
		}
		for (ItemOption itemOption : itemOptionList) {
			final String key = itemOption.getItem().getId() + DELIMITER + itemOption.getId();
			final Integer orderCount = map.get(key);
			cartResponseList.add(this.itemOptionAndOrderCountToCartResponse(itemOption, orderCount));
		}
		return cartResponseList;
	}

	@Mappings({
		@Mapping(source = "itemOption.item.id", target = "itemId"),
		@Mapping(source = "itemOption.id", target = "itemOptionId"),
		@Mapping(source = "orderCount", target = "orderCount"),
		@Mapping(source = "itemOption.description", target = "itemOptionDescription"),
		@Mapping(source = "itemOption.item.name", target = "itemName"),
		@Mapping(source = "itemOption.item.basePrice", target = "basePrice"),
		@Mapping(source = "itemOption.optionPrice", target = "optionPrice"),
		@Mapping(source = "itemOption.stockQuantity", target = "stockQuantity"),
		@Mapping(source = "itemOption.item.category.name", target = "category"),
		@Mapping(target = "itemThumbnailUrl", expression = "java(addPrefixToThumbnail(itemOption.getItem()))"),
	})
	public abstract CartResponse itemOptionAndOrderCountToCartResponse(ItemOption itemOption, Integer orderCount);
}
