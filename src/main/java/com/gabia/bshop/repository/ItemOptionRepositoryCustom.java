package com.gabia.bshop.repository;

import java.util.List;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.OrderItem;

public interface ItemOptionRepositoryCustom {

	List<ItemOption> findAllByItemIdsAndItemOptionIds(List<CartDto> cartDtoList);

	List<ItemOption> findByItemIdListAndIdListWithLock(List<OrderItemDto> orderItemDtoList);

	List<ItemOption> findByItemIdListAndIdListInOrderItemListWithLock(List<OrderItem> orderItemList);

	List<ItemOption> findByItemIdListAndIdList(List<OrderItemDto> orderItemDtoList);
}
