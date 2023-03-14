package com.gabia.bshop.service;

import static com.gabia.bshop.fixture.ItemOptionFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.fixture.CategoryFixture;
import com.gabia.bshop.fixture.ItemFixture;
import com.gabia.bshop.repository.CartRepository;
import com.gabia.bshop.repository.ItemOptionRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	private static final String MINIO_PREFIX = "http://localhost:9000/images";

	private final Long memberId = 1L;
	private final Long itemId = 1L;
	private final Long itemOptionId = 1L;
	private final Long newItemId = 2L;
	private final Long newItemOptionId = 2L;
	private final int orderCount = 1;
	private final int newOrderCount = 2;

	@InjectMocks
	private CartService cartService;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ItemOptionRepository itemOptionRepository;

	@Test
	@DisplayName("장바구니를 저장한다")
	void given_CartDto_when_save_then_return_savedCartDto() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		given(itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId, itemOptionId,
			orderCount)).willReturn(Boolean.TRUE);
		given(cartRepository.save(memberId, cartDto)).willReturn(cartDto);

		// when
		final CartDto savedCartDto = cartService.createCart(memberId, cartDto);

		// then
		assertAll(
			() -> verify(cartRepository).save(memberId, cartDto),
			() -> assertThat(savedCartDto).usingRecursiveComparison().isEqualTo(cartDto)
		);
	}

	@Test
	@DisplayName("장바구니에 존재하지 않는 아이템을 넣으려고하면 예외를 던진다.")
	void given_InvalidItem_when_save_then_return_throw_exception() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		given(itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId, itemOptionId,
			orderCount)).willReturn(Boolean.FALSE);

		// when & then
		assertAll(
			() -> assertThatThrownBy(() -> cartService.createCart(memberId, cartDto)).isInstanceOf(
				NotFoundException.class),
			() -> verify(itemOptionRepository).existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId,
				itemOptionId, orderCount)
		);
	}

	@Test
	@DisplayName("장바구니에 존재하지 않는 옵션을 넣으려고하면 예외를 던진다.")
	void given_InvalidItemOption_when_save_then_return_throw_exception() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		given(itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId, itemOptionId,
			orderCount)).willReturn(Boolean.FALSE);

		// when & then
		assertAll(
			() -> assertThatThrownBy(() -> cartService.createCart(memberId, cartDto)).isInstanceOf(
				NotFoundException.class),
			() -> verify(itemOptionRepository).existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId,
				itemOptionId, orderCount)
		);
	}

	@Test
	@DisplayName("장바구니를 저장할 때 재고가 없을 때 예외를 던진다")
	void given_InvalidOrderCount_when_save_then_return_throw_exception() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		given(itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId, itemOptionId,
			orderCount)).willReturn(Boolean.FALSE);

		// when & then
		assertAll(
			() -> assertThatThrownBy(() -> cartService.createCart(memberId, cartDto)).isInstanceOf(
				NotFoundException.class),
			() -> verify(itemOptionRepository).existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(itemId,
				itemOptionId, orderCount)
		);
	}

	@Test
	@DisplayName("장바구니에 담긴 정보를 통해, 개략적인 상품 정보를 반환한다")
	void given_SavedItems_when_findAll_then_return_List_of_CartResponse() {
		// given
		final CartDto cartDto1 = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto cartDto2 = new CartDto(newItemId, newItemOptionId, newOrderCount);

		final Category category1 = CategoryFixture.CATEGORY_1.getInstance();
		final Item item1 = ItemFixture.ITEM_1.getInstance(itemId, category1);
		final ItemOption itemOption1 = ITEM_OPTION_1.getInstance(itemOptionId, item1);
		final Item item2 = ItemFixture.ITEM_2.getInstance(newItemId, category1);
		final ItemOption itemOption2 = ITEM_OPTION_2.getInstance(newItemOptionId, item2);

		final CartResponse cartResponse1 = new CartResponse(itemId, itemOptionId, cartDto1.orderCount(),
			itemOption1.getDescription(), item1.getName(), item1.getBasePrice(),
			itemOption1.getOptionPrice(), itemOption1.getStockQuantity(), category1.getName(),
			MINIO_PREFIX + "/" + item1.getThumbnail());
		final CartResponse cartResponse2 = new CartResponse(newItemId, newItemOptionId, cartDto2.orderCount(),
			itemOption2.getDescription(), item2.getName(), item2.getBasePrice(),
			itemOption2.getOptionPrice(), itemOption2.getStockQuantity(), category1.getName(),
			MINIO_PREFIX + "/" + item2.getThumbnail());

		given(cartRepository.findAllByMemberId(memberId)).willReturn(List.of(cartDto1, cartDto2));
		given(itemOptionRepository.findAllByItemIdsAndItemOptionIds(List.of(cartDto1, cartDto2)))
			.willReturn(List.of(itemOption1, itemOption2));

		// when
		final List<CartResponse> actual = cartService.findCartList(memberId);

		// then
		assertAll(
			() -> assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(cartResponse1, cartResponse2))
		);
	}

	@Test
	@DisplayName("장바구니에 저장된 정보를 개별로 삭제한다")
	void given_SavedItems_when_delete_then_return_void() {
		// given
		final CartDto cartDto = new CartDto(itemId, itemOptionId, orderCount);
		willDoNothing().given(cartRepository).delete(memberId, cartDto);

		// when
		cartService.deleteCart(memberId, cartDto);

		// then
		then(cartRepository).should().delete(memberId, cartDto);
	}

	@Test
	@DisplayName("장바구니에서 담긴 정보들 여러개를 삭제한다")
	void given_SavedItems_when_deleteCartList_then_return_void() {
		// given
		final CartDto cartDto1 = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto cartDto2 = new CartDto(itemId, itemOptionId, orderCount);
		final CartDto cartDto3 = new CartDto(itemId, itemOptionId, orderCount);
		willDoNothing().given(cartRepository).deleteAll(memberId, List.of(cartDto1, cartDto2, cartDto3));

		// when
		cartService.deleteCartList(memberId, List.of(cartDto1, cartDto2, cartDto3));

		// then
		then(cartRepository).should().deleteAll(memberId, List.of(cartDto1, cartDto2, cartDto3));
	}
}
