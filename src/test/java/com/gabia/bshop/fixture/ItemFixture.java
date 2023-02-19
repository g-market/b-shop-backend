package com.gabia.bshop.fixture;

import java.time.LocalDateTime;

import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.enumtype.ItemStatus;

public enum ItemFixture {

	ITEM_1("아이템 1", "아이템 1 description", 10000, ItemStatus.PUBLIC,
		LocalDateTime.now(), false),
	ITEM_2("아이템 2", "아이템 2 description", 20000, ItemStatus.PUBLIC,
		LocalDateTime.now(), false),
	ITEM_3("아이템 3", "아이템 3 description", 30000, ItemStatus.PUBLIC,
		LocalDateTime.now(), false),
	ITEM_4("아이템 4", "아이템 4 description", 40000, ItemStatus.PRIVATE,
		LocalDateTime.now(), false),
	ITEM_5("아이템 5", "아이템 5 description", 50000, ItemStatus.RESERVED,
		LocalDateTime.now().plusDays(3L), false),
	;

	private final String name;
	private final String description;
	private final int basePrice;
	private final ItemStatus itemStatus;
	private final LocalDateTime openAt;
	private final boolean deleted;

	ItemFixture(final String name, final String description, final int basePrice, final ItemStatus itemStatus,
		final LocalDateTime openAt,
		final boolean deleted) {
		this.name = name;
		this.description = description;
		this.basePrice = basePrice;
		this.itemStatus = itemStatus;
		this.openAt = openAt;
		this.deleted = deleted;
	}

	public Item getInstance(final Category category) {
		return getInstance(null, category);
	}

	public Item getInstance(final Long itemId, final Category category) {
		return Item.builder()
			.id(itemId)
			.category(category)
			.name(name)
			.description(description)
			.basePrice(basePrice)
			.itemStatus(itemStatus)
			.openAt(openAt)
			.deleted(deleted)
			.build();
	}
}
