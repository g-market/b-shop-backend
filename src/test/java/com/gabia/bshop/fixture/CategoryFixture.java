package com.gabia.bshop.fixture;

import com.gabia.bshop.entity.Category;

public enum CategoryFixture {

	CATEGORY_1("카테고리 1"),
	CATEGORY_2("카테고리 2"),
	CATEGORY_3("카테고리 3"),
	CATEGORY_4("카테고리 4"),
	CATEGORY_5("카테고리 5");

	private final String name;

	CategoryFixture(final String name) {
		this.name = name;
	}

	public Category getInstance() {
		return getInstance(null);
	}

	public Category getInstance(final Long categoryId) {
		return Category.builder()
			.id(categoryId)
			.name(name)
			.build();
	}
}
