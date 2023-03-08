package com.gabia.bshop.repository;

import java.util.List;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.OrderItemAble;

public interface CartRepository {

	CartDto save(Long memberId, CartDto cartDto);

	List<CartDto> findAllByMemberId(Long memberId);

	void delete(Long memberId, CartDto cartDto);

	<T extends OrderItemAble> void deleteAll(Long memberId, List<T> orderItemAbleList);
}
