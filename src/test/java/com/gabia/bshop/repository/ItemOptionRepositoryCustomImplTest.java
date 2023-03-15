package com.gabia.bshop.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.bshop.dto.CartDto;

@SpringBootTest
class ItemOptionRepositoryCustomImplTest {

	@Autowired
	ItemOptionRepository itemOptionRepository;

	@Test
	@DisplayName("단순 쿼리 확인용 테스트 코드")
	void findWithItemAndCategoryAndImageByItemIdListAndIdList() {
		final CartDto cartDto1 = new CartDto(1L, 1L, 3);
		final CartDto cartDto2 = new CartDto(2L, 3L, 3);
		final CartDto cartDto3 = new CartDto(2L, 4L, 3);
		itemOptionRepository.findAllByItemIdsAndItemOptionIds(
			List.of(cartDto1, cartDto2, cartDto3));
	}
}
