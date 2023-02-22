package com.gabia.bshop.repository;

import java.util.List;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepositoryCustom {

	List<ItemOption> findWithItemAndCategoryAndImageByItemIdListAndIdList(List<CartDto> cartDtoList);
}
