package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QCategory.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.gabia.bshop.entity.Category;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<String> findCategoryNames() {
		return jpaQueryFactory.select(category.name)
			.from(category)
			.where(category.deleted.eq(false))
			.orderBy(category.name.asc())
			.fetch();
	}

	@Override
	public Page<Category> findCategoryList(Pageable pageable) {
		final List<Category> contents = jpaQueryFactory.select(category)
			.from(category)
			.where(category.deleted.eq(false))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final JPAQuery<Long> countQuery = jpaQueryFactory.select(category.count())
			.from(category)
			.where(category.deleted.eq(false));

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Category> findCategoryListWithDeleted(Pageable pageable) {
		final List<Category> contents = jpaQueryFactory.select(category)
			.from(category)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final JPAQuery<Long> countQuery = jpaQueryFactory.select(category.count())
			.from(category);

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}
}
