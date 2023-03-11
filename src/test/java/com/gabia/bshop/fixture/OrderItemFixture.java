package com.gabia.bshop.fixture;

import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;

public enum OrderItemFixture {

	ORDER_ITEM_1(10, 10000),
	ORDER_ITEM_2(20, 20000),
	ORDER_ITEM_3(30, 30000),
	ORDER_ITEM_4(40, 40000),
	ORDER_ITEM_5(50, 50000);

	private final int orderCount;
	private final long price;

	OrderItemFixture(final int orderCount, final long price) {
		this.orderCount = orderCount;
		this.price = price;
	}

	public OrderItem getInstance(final Item item, final Order order, final ItemOption itemOption) {
		return getInstance(null, item, order, itemOption);
	}

	public OrderItem getInstance(final Long orderItemId, final Item item, final Order order,
		final ItemOption itemOption) {
		return OrderItem.builder()
			.id(orderItemId)
			.item(item)
			.order(order)
			.option(itemOption)
			.orderCount(orderCount)
			.price(price)
			.build();
	}
}
