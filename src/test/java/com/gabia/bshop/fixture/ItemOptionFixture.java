package com.gabia.bshop.fixture;

import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;

public enum ItemOptionFixture {

	ITEM_OPTION_1("아이템 옵션 1", 1000, 100),
	ITEM_OPTION_2("아이템 옵션 2", 2000, 200),
	ITEM_OPTION_3("아이템 옵션 3", 3000, 300),
	ITEM_OPTION_4("아이템 옵션 4", 4000, 400),
	ITEM_OPTION_5("아이템 옵션 5", 5000, 500),
	;

	private final String description;
	private final int optionPrice;
	private final int stockQuantity;

	ItemOptionFixture(final String description, final int optionPrice, final int stockQuantity) {
		this.description = description;
		this.optionPrice = optionPrice;
		this.stockQuantity = stockQuantity;
	}

	public ItemOption getInstance(final Item item) {
		return getInstance(null, item);
	}

	public ItemOption getInstance(final Long itemOptionId, final Item item) {
		return ItemOption.builder()
			.id(itemOptionId)
			.item(item)
			.description(description)
			.optionPrice(optionPrice)
			.stockQuantity(stockQuantity)
			.build();
	}
}
