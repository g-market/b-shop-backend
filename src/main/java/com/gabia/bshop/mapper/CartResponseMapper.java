package com.gabia.bshop.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.ItemOption;

@Mapper(componentModel = "spring")
public interface CartResponseMapper {

	CartResponseMapper INSTANCE = Mappers.getMapper(CartResponseMapper.class);

	default List<CartResponse> from(final List<ItemOption> itemOptionList, final List<CartDto> sortedCartDtoList) {
		final List<CartResponse> cartResponseList = new ArrayList<>();
		for (int i = 0; i < itemOptionList.size(); i++) {
			final ItemOption itemOption = itemOptionList.get(i);
			final CartDto cartDto = sortedCartDtoList.get(i);
			cartResponseList.add(CartResponse.builder()
				.itemId(cartDto.itemId())
				.itemOptionId(cartDto.itemOptionId())
				.orderCount(cartDto.orderCount())
				.name(itemOption.getItem().getName())
				.basePrice(itemOption.getItem().getBasePrice())
				.optionPrice(itemOption.getOptionPrice())
				.category(itemOption.getItem().getCategory().getName())
				.imageUrl("추후에 기입")
				.build());
		}
		return cartResponseList;
	}
}
