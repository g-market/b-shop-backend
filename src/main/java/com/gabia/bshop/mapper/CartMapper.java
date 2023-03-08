package com.gabia.bshop.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.request.CartCreateRequest;
import com.gabia.bshop.dto.request.CartDeleteRequest;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public interface CartMapper {

	CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

	String DELIMITER = "-";

	CartDto cartCreateRequestToCartDto(final CartCreateRequest cartCreateRequest);

	@Mappings({
		@Mapping(target = "orderCount", ignore = true)
	})
	CartDto cartDeleteRequestToCartDto(final CartDeleteRequest cartDeleteRequest);

	default List<CartResponse> itemOptionListAndCartDtoToCartResponse(final List<ItemOption> itemOptionList,
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
		@Mapping(source = "itemOption.item.name", target = "name"),
		@Mapping(source = "itemOption.item.basePrice", target = "basePrice"),
		@Mapping(source = "itemOption.optionPrice", target = "optionPrice"),
		@Mapping(source = "itemOption.item.category.name", target = "category"),
		@Mapping(source = "itemOption.item.thumbnail", target = "thumbnailUrl"),
	})
	CartResponse itemOptionAndOrderCountToCartResponse(ItemOption itemOption, Integer orderCount);
}
