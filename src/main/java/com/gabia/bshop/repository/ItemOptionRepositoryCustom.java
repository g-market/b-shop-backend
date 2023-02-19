package com.gabia.bshop.repository;

import java.util.Collection;
import java.util.List;

import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepositoryCustom {

	List<ItemOption> findWithItemByItemIdsAndItemOptionIds(List<Long> itemIdList, List<Long> itemOptionIdList);

	List<ItemOption> findWithItemAndCategory(Collection<Long> itemIdList, Collection<Long> itemOptionIdList);
}
