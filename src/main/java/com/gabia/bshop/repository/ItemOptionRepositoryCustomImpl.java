package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QCategory.*;
import static com.gabia.bshop.entity.QItem.*;
import static com.gabia.bshop.entity.QItemOption.*;

import java.util.Collection;
import java.util.List;

import com.gabia.bshop.entity.ItemOption;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemOptionRepositoryCustomImpl implements ItemOptionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ItemOption> findWithItemByItemIdsAndItemOptionIds(List<Long> itemIdList, List<Long> itemOptionIdList) {
		return jpaQueryFactory.select(itemOption)
			.from(itemOption)
			.join(itemOption.item)
			.where(item.id.in(itemIdList).and(itemOption.id.in(itemOptionIdList)))
			.fetch();
	}

	@Override
	public List<ItemOption> findWithItemAndCategory(Collection<Long> itemIdList, Collection<Long> itemOptionIdList) {
		return jpaQueryFactory.select(itemOption)
			.from(itemOption)
			.join(itemOption.item, item).fetchJoin()
			.join(item.category, category).fetchJoin()
			// .join(item.image, itemImage).fetchJoin()
			.where(item.id.in(itemIdList).and(itemOption.id.in(itemOptionIdList)))
			.orderBy(item.id.asc(), itemOption.id.asc())
			.fetch();
	}
}
