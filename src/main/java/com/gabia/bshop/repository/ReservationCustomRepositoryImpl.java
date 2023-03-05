package com.gabia.bshop.repository;


import static com.gabia.bshop.entity.QItem.*;
import static com.gabia.bshop.entity.QReservation.*;

import java.util.List;

import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public void deleteAllReservation(final List<Reservation> reservationList) {
		jpaQueryFactory.delete(reservation)
			.where(reservation.in(reservationList))
			.execute();
	}

	@Override
	public void updateAllItemStatus(final List<Long> itemIdList, final ItemStatus itemStatus) {
		jpaQueryFactory.update(item)
			.where(item.id.in(itemIdList))
			.set(item.itemStatus, itemStatus)
			.execute();
	}
}
