package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.OrderItemAble;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.CartResponseMapper;
import com.gabia.bshop.repository.CartRepository;
import com.gabia.bshop.repository.ItemOptionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

	private final CartRepository cartRepository;
	private final ItemOptionRepository itemOptionRepository;

	public List<CartResponse> findCartList(final Long memberId) {
		final List<CartDto> cartDtoList = cartRepository.findAllByMemberId(memberId);
		if (cartDtoList.isEmpty()) {
			return Collections.emptyList();
		}
		final List<ItemOption> itemOptionList = itemOptionRepository.findWithItemAndCategoryAndImageByItemIdListAndIdList(
			cartDtoList);
		return CartResponseMapper.INSTANCE.from(itemOptionList, cartDtoList);
	}

	@Transactional
	public CartDto createCart(final Long memberId, final CartDto cartDto) {
		checkItemAndItemOption(cartDto);
		return cartRepository.save(memberId, cartDto);
	}

	private void checkItemAndItemOption(final CartDto cartDto) {
		if (!itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(cartDto.itemId(),
			cartDto.itemOptionId(), cartDto.orderCount())) {
			throw new NotFoundException(ITEM_OPTION_NOT_FOUND_EXCEPTION, cartDto.itemId(), cartDto.itemOptionId());
		}
	}

	@Transactional
	public void deleteCart(final Long memberId, final CartDto cartDto) {
		cartRepository.delete(memberId, cartDto);
	}

	@Transactional
	public <T extends OrderItemAble> void deleteCartList(final Long memberId, final List<T> orderItemAbleList) {
		cartRepository.deleteAllByItemIdAndItemOptionId(memberId, orderItemAbleList);
	}
}
