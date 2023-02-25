package com.gabia.bshop.repository;

import java.util.List;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepositoryCustom {

	List<ItemOption> findWithItemAndCategoryAndImageByItemIdListAndIdList(List<CartDto> cartDtoList);

	List<ItemOption> findByItemIdListAndIdList(List<OrderItemDto> orderItemDtoList);

	List<ItemOption> findByItemIdListAndIdListWithLock(List<OrderItemDto> orderItemDtoList);
}
