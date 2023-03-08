package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QCategory.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<String> findCategoryNames() {
		return jpaQueryFactory.select(category.name)
			.from(category)
			.orderBy(category.name.asc())
			.fetch();
	}
}
