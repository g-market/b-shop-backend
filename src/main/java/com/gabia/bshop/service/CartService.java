package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CartDto;
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

	@Transactional
	public CartDto save(final Long memberId, final CartDto cartDto) {
		checkItemAndItemOption(cartDto);
		return cartRepository.save(memberId, cartDto);
	}

	private void checkItemAndItemOption(final CartDto cartDto) {
		if (!itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(cartDto.itemId(),
			cartDto.itemOptionId(), cartDto.orderCount())) {
			throw new NotFoundException(ITEM_ITEMOPTION_NOT_FOUND_EXCEPTION);
		}
	}

	public List<CartResponse> findAll(final Long memberId) {
		final List<CartDto> cartDtoList = cartRepository.findAllByMemberId(memberId);
		final Set<Long> itemIdList = new HashSet<>();
		final Set<Long> itemOptionIdList = new HashSet<>();
		for (CartDto cartDto : cartDtoList) {
			itemIdList.add(cartDto.itemId());
			itemOptionIdList.add(cartDto.itemOptionId());
		}

		final List<CartDto> sortedCartDtoList = cartDtoList.stream()
			.sorted(Comparator.comparing(CartDto::itemId).thenComparing(CartDto::itemOptionId))
			.toList();

		final List<ItemOption> itemOptionList = itemOptionRepository.findWithItemAndCategory(itemIdList,
			itemOptionIdList);

		return CartResponseMapper.INSTANCE.from(itemOptionList, sortedCartDtoList);
	}

	@Transactional
	public void delete(final Long memberId, final CartDto cartDto) {
		cartRepository.delete(memberId, cartDto);
	}
}
