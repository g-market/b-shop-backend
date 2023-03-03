package com.gabia.bshop.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.OrderItemAble;
import com.gabia.bshop.util.RedisValueSupport;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CartRepositoryImpl implements CartRepository {

	public static final String CART_PREFIX = "cart:memberId-";
	private static final String DELIMITER = "-";

	private final RedisTemplate<String, String> redisTemplate;

	private final HashOperations<String, String, String> hashOperations;

	private final RedisValueSupport redisValueSupport;

	@Value("${cart.expired-time}")
	private long expiredTimeMillis;

	@Override
	@Transactional
	public CartDto save(final Long memberId, final CartDto cartDto) {
		final String key = getKey(memberId);
		final String hashKey = getHashKey(cartDto);
		final String value = redisValueSupport.writeValueAsString(cartDto);
		if (!existsCartByKeyAndHashKey(key, hashKey)) {
			hashOperations.put(key, hashKey, value);
		} else {
			final CartDto savedValue = getCartDtoValue(key, hashKey);
			if (savedValue.orderCount() != cartDto.orderCount()) {
				hashOperations.put(key, hashKey, value);
			}
		}
		redisTemplate.expire(key, expiredTimeMillis, TimeUnit.MILLISECONDS);
		return cartDto;
	}

	@Override
	public List<CartDto> findAllByMemberId(final Long memberId) {
		final String key = getKey(memberId);
		final Map<String, String> entries = hashOperations.entries(key);
		if (entries.size() == 0) {
			return Collections.emptyList();
		}
		return entries.values().stream()
			.map(this::toCartDto)
			.toList();
	}

	@Override
	@Transactional
	public void delete(final Long memberId, final CartDto cartDto) {
		final String key = getKey(memberId);
		final String hashKey = getHashKey(cartDto);
		if (!existsCartByKeyAndHashKey(key, hashKey)) {
			return;
		}
		hashOperations.delete(key, hashKey);
	}

	@Override
	@Transactional
	public <T extends OrderItemAble> void deleteAllByItemIdAndItemOptionId(final Long memberId,
		final List<T> orderItemAbleList) {
		final String key = getKey(memberId);
		for (final OrderItemAble orderItemAble : orderItemAbleList) {
			final String hashKey = getHashKey(orderItemAble);
			hashOperations.delete(key, hashKey);
		}
	}

	private String getKey(final Long memberId) {
		return CART_PREFIX + memberId;
	}

	private String getHashKey(final OrderItemAble orderItemAble) {
		return orderItemAble.itemId() + DELIMITER + orderItemAble.itemOptionId();
	}

	private boolean existsCartByKeyAndHashKey(final String key, final String hashKey) {
		return hashOperations.get(key, hashKey) != null;
	}

	private CartDto getCartDtoValue(final String key, final String hashKey) {
		return redisValueSupport.readValue(hashOperations.get(key, hashKey), CartDto.class);
	}

	private CartDto toCartDto(final String value) {
		return redisValueSupport.readValue(value, CartDto.class);
	}
}
