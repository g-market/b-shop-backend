package com.gabia.bshop.repository;

import static com.gabia.bshop.entity.QItem.*;
import static com.gabia.bshop.entity.QReservation.*;

import java.util.List;

import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationCustomImpl implements ReservationCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public void updateAllItemStatusAndDeleteReservation(final List<Long> reservationIdList, final List<Long> itemIdList,
		final ItemStatus itemStatus) {
		jpaQueryFactory.delete(reservation)
			.where(reservation.id.in(reservationIdList))
			.execute();

		jpaQueryFactory.update(item)
			.where(item.id.in(itemIdList))
			.set(item.itemStatus, itemStatus)
			.execute();
	}
}
