package com.gabia.bshop.fixture;

import static com.gabia.bshop.entity.enumtype.OrderStatus.*;

import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.enumtype.OrderStatus;

public enum OrderFixture {

	ORDER_1(ACCEPTED, 10000),
	ORDER_2(ACCEPTED, 20000),
	ORDER_3(ACCEPTED, 30000),
	ORDER_4(COMPLETED, 40000),
	ORDER_5(CANCELLED, 50000);

	private final OrderStatus orderStatus;
	private final long totalPrice;

	OrderFixture(final OrderStatus orderStatus, final long totalPrice) {
		this.orderStatus = orderStatus;
		this.totalPrice = totalPrice;
	}

	public Order getInstance(final Member member) {
		return getInstance(null, member);
	}

	public Order getInstance(final Long orderId, final Member member) {
		return Order.builder()
			.id(orderId)
			.member(member)
			.status(orderStatus)
			.totalPrice(totalPrice)
			.build();
	}
}
