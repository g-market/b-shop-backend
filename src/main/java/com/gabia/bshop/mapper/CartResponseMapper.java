package com.gabia.bshop.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public interface CartResponseMapper {

	CartResponseMapper INSTANCE = Mappers.getMapper(CartResponseMapper.class);

	default List<CartResponse> from(final List<ItemOption> itemOptionList, final List<CartDto> cartDtoList) {
		final List<CartResponse> cartResponseList = new ArrayList<>();
		final HashMap<String, Integer> map = new HashMap<>();
		for (CartDto cartDto : cartDtoList) {
			final String key = String.valueOf(cartDto.itemId()) + cartDto.itemOptionId();
			map.put(key, cartDto.orderCount());
		}
		for (ItemOption itemOption : itemOptionList) {
			final String key = String.valueOf(itemOption.getItem().getId()) + itemOption.getId();
			final Integer orderCount = map.get(key);
			cartResponseList.add(this.from(itemOption, orderCount));
		}
		return cartResponseList;
	}

	@Mappings({
		@Mapping(source = "itemOption.item.id", target = "itemId"),
		@Mapping(source = "itemOption.id", target = "itemOptionId"),
		@Mapping(source = "orderCount", target = "orderCount"),
		@Mapping(source = "itemOption.item.name", target = "name"),
		@Mapping(source = "itemOption.item.basePrice", target = "basePrice"),
		@Mapping(source = "itemOption.optionPrice", target = "optionPrice"),
		@Mapping(source = "itemOption.item.category.name", target = "category"),
		@Mapping(source = "itemOption.item.thumbnail", target = "thumbnailUrl"),
	})
	CartResponse from(ItemOption itemOption, Integer orderCount);
}
