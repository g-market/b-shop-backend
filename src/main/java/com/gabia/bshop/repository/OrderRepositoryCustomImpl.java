package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QOrder.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.gabia.bshop.dto.searchConditions.OrderSearchConditions;
import com.gabia.bshop.entity.Order;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Order> findAllBySearchConditions(final Pageable pageable,
		final OrderSearchConditions orderSearchConditions,
		final Long memberId) {
		final List<Order> contents = jpaQueryFactory.select(order)
			.from(order)
			.where(betweenDate(orderSearchConditions), eqMemberId(memberId))
			.offset(pageable.getOffset())
			// .orderBy(order.id.desc())
			.limit(pageable.getPageSize())
			.fetch();

		final JPAQuery<Long> countQuery = jpaQueryFactory.select(order.count())
			.from(order)
			.where(betweenDate(orderSearchConditions), eqMemberId(memberId));

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	private BooleanExpression betweenDate(OrderSearchConditions orderSearchConditions) {
		if (orderSearchConditions.startDate() == null && orderSearchConditions.endDate() == null) {
			return null;
		}
		if (orderSearchConditions.startDate() == null) {
			return order.createdAt.before(orderSearchConditions.endDate());
		}
		if (orderSearchConditions.endDate() == null) {
			return order.createdAt.after(orderSearchConditions.startDate());
		}
		return order.createdAt.between(orderSearchConditions.startDate(), orderSearchConditions.endDate());
	}

	private BooleanExpression eqMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		return order.member.id.eq(memberId);
	}
}
