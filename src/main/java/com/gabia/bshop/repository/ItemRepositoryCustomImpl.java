package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QCategory.*;
import static com.gabia.bshop.entity.QItem.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.gabia.bshop.dto.searchConditions.ItemSearchConditions;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Integer> findItemYears() {
		return jpaQueryFactory.select(item.year).distinct()
			.from(item)
			.orderBy(item.year.desc())
			.fetch();
	}

	@Override
	public Page<Item> findItemListByItemSearchConditions(Pageable pageable, ItemSearchConditions itemSearchConditions) {
		final List<Item> contents = jpaQueryFactory.select(item)
			.from(item)
			.join(item.category, category).fetchJoin()
			.where(eqCategoryName(itemSearchConditions.categoryName()),
				containsItemName(itemSearchConditions.itemName()),
				eqItemYear(itemSearchConditions.year()),
				item.deleted.eq(false), item.itemStatus.ne(ItemStatus.PRIVATE))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
			.from(item)
			.where(eqCategoryName(itemSearchConditions.categoryName()),
				containsItemName(itemSearchConditions.itemName()),
				eqItemYear(itemSearchConditions.year()),
				item.deleted.eq(false), item.itemStatus.ne(ItemStatus.PRIVATE));

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Item> findItemListWithDeletedByItemSearchConditions(Pageable pageable,
		ItemSearchConditions itemSearchConditions) {
		final List<Item> contents = jpaQueryFactory.select(item)
			.from(item)
			.join(item.category, category).fetchJoin()
			.where(eqCategoryName(itemSearchConditions.categoryName()),
				containsItemName(itemSearchConditions.itemName()),
				eqItemYear(itemSearchConditions.year()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
			.from(item)
			.where(eqCategoryName(itemSearchConditions.categoryName()),
				containsItemName(itemSearchConditions.itemName()),
				eqItemYear(itemSearchConditions.year()));

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	private BooleanExpression eqCategoryName(String categoryName) {
		if (StringUtils.hasText(categoryName)) {
			return category.name.eq(categoryName);
		}
		return null;
	}

	private BooleanExpression eqItemYear(Integer year) {
		if (year != null) {
			return item.year.eq(year);
		}
		return null;
	}

	private BooleanExpression containsItemName(String itemName) {
		if (StringUtils.hasText(itemName)) {
			return item.name.contains(itemName);
		}
		return null;
	}
}
