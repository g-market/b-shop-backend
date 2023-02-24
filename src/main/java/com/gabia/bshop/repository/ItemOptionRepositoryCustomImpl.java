package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QCategory.*;
import static com.gabia.bshop.entity.QItem.*;
import static com.gabia.bshop.entity.QItemOption.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Lock;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.ItemIdAndItemOptionIdAble;
import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.entity.ItemOption;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemOptionRepositoryCustomImpl implements ItemOptionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ItemOption> findWithItemAndCategoryAndImageByItemIdListAndIdList(List<CartDto> cartDtoList) {
		return jpaQueryFactory.select(itemOption)
			.from(itemOption)
			.join(itemOption.item, item).fetchJoin()
			.join(item.category, category).fetchJoin()
			.where(Expressions.list(item.id, itemOption.id).in(searchItemIdAndItemOptionIdIn(cartDtoList)))
			.fetch();
	}

	@Override
	public List<ItemOption> findByItemIdListAndIdList(List<OrderItemDto> orderItemDtoList) {
		return jpaQueryFactory.select(itemOption)
			.from(itemOption)
			.where(Expressions.list(item.id, itemOption.id).in(searchItemIdAndItemOptionIdIn(orderItemDtoList)))
			.orderBy(item.id.asc(), itemOption.id.asc())
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetch();
	}

	private BooleanExpression itemIdEq(Long itemId) {
		return itemId != null ? item.id.eq(itemId) : null;
	}

	private BooleanExpression itemOptionIdEq(Long itemOptionId) {
		return itemOptionId != null ? itemOption.id.eq(itemOptionId) : null;
	}

	private <T extends ItemIdAndItemOptionIdAble> Expression[] searchItemIdAndItemOptionIdIn(
		List<T> itemIdAndItemOptionIdList) {
		List<Expression<Object>> tuples = new ArrayList<>();
		for (T cartDto : itemIdAndItemOptionIdList) {
			tuples.add(Expressions.template(Object.class, "(({0}, {1}))", cartDto.itemId(), cartDto.itemOptionId()));
		}
		return tuples.toArray(new Expression[0]);
	}
}
