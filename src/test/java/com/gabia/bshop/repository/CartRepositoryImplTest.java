package com.gabia.bshop.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.integration.IntegrationTest;

@DisplayName("[Redis] 장바구니 Repository 테스트")
class CartRepositoryImplTest extends IntegrationTest {

	private final Long memberId = 1L;
	private final Long itemId = 1L;
	private final Long itemOptionId = 1L;
	private final Long newItemId = 1L;
	private final Long newItemOptionId = 2L;
	private final int orderCount = 2;
	private final int newOrderCount = 3;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Value("${cart.expired-time}")
	private long expiredTimeMillis;

	@BeforeEach
	void setUp() {
		final RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
		assert connectionFactory != null;
		connectionFactory.getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("장바구니를 저장한다")
	void given_CartDto_when_save_then_return_savedCartDto() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);

		// when
		final CartDto savedCartDto = cartRepository.save(memberId, cartDto);

		// then
		assertThat(savedCartDto).isEqualTo(cartDto);
	}

	@Test
	@DisplayName("비어 있는 장바구니를 조회하면 빈 리스트가 조회된다")
	void given_nothing_when_findCart_then_return_emptyList() {
		// when & then
		final List<CartDto> actual = cartRepository.findAllByMemberId(memberId);

		assertAll(
			() -> assertThat(actual).isEmpty(),
			() -> assertThat(actual).usingRecursiveComparison().isEqualTo(Collections.emptyList())
		);
	}

	@Test
	@DisplayName("장바구니를 조회한다")
	void given_savedCarts_when_findCart_then_return_CartDtoList() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto cartDto2 = new CartDto(newItemId, newItemOptionId, newOrderCount);
		cartRepository.save(memberId, cartDto);
		cartRepository.save(memberId, cartDto2);

		// when
		final List<CartDto> actual = cartRepository.findAllByMemberId(memberId);

		// then
		assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(cartDto, cartDto2));
	}

	@Test
	@DisplayName("장바구니를 삭제한다")
	void given_savedCarts_when_deleteCart_then_return_CartDtoList() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto cartDto2 = new CartDto(newItemId, newItemOptionId, newOrderCount);
		cartRepository.save(memberId, cartDto);
		cartRepository.save(memberId, cartDto2);

		// when
		cartRepository.delete(memberId, cartDto);

		// then
		final List<CartDto> actual = cartRepository.findAllByMemberId(memberId);
		assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(cartDto2));
	}

	@Test
	@DisplayName("장바구니에 ItemId, ItemOptionId, orderCount가 같을시에 TTL만 갱신한다")
	void given_savedCart_when_saveSameCartDto_then_return_SameCartDto() {
		// given
		final String cartPrefix = "cart:memberId-";
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto sameCartDto = new CartDto(itemId, itemOptionId, orderCount);
		cartRepository.save(memberId, cartDto);

		// when
		cartRepository.save(memberId, sameCartDto);

		// then
		final Long expireActual = redisTemplate.getExpire(cartPrefix + memberId, TimeUnit.MILLISECONDS);
		final List<CartDto> actual = cartRepository.findAllByMemberId(memberId);

		assertAll(
			() -> assertThat(expireActual).isCloseTo(expiredTimeMillis, withinPercentage(5L)),
			() -> assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(cartDto))
		);
	}

	@Test
	@DisplayName("장바구니 같은 아이템과 같은 아이템 옵션일 때, orderCount가 다를시에 TTL을 갱신하고 장바구니를 변경한다")
	void given_savedCart_when_saveDifferentOrderCount_then_return_NewCartDto() {
		// given
		final String cartPrefix = "cart:memberId-";
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto differentCartDto = new CartDto(itemId, itemOptionId, newOrderCount);
		cartRepository.save(memberId, cartDto);

		// when
		cartRepository.save(memberId, differentCartDto);

		// then
		final Long expireActual = redisTemplate.getExpire(cartPrefix + memberId, TimeUnit.MILLISECONDS);
		final List<CartDto> actual = cartRepository.findAllByMemberId(memberId);

		assertAll(
			() -> assertThat(expireActual).isCloseTo(expiredTimeMillis, withinPercentage(5L)),
			() -> assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(differentCartDto))
		);
	}
}
