package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Lock;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.ItemOption;

import jakarta.persistence.LockModeType;

public interface ItemOptionRepositoryCustom {

	List<ItemOption> findWithItemAndCategoryAndImageByItemIdListAndIdList(List<CartDto> cartDtoList);

	List<ItemOption> findByItemIdListAndIdList(List<OrderItemDto> orderItemDtoList);

	List<ItemOption> findByItemIdListAndIdListWithLock(List<OrderItemDto> orderItemDtoList);
}
