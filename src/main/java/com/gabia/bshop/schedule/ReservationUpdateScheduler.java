package com.gabia.bshop.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.repository.ItemReserveRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationUpdateScheduler {
	private final ItemReserveRepository itemReserveRepository;

	@Scheduled(cron = "0 * * * * *") // 1분 마다 실행
	@SchedulerLock(
		name = "scheduledItemStatusUpdateTask",
		lockAtLeastFor = "5s", // 최소 잠금 시간
		lockAtMostFor = "10s") // 최대 잠금 시간
	@Transactional
	public void updateReservationStatus() {
		final List<Reservation> reservationList = itemReserveRepository.findAllByItemOpenAtBefore(LocalDateTime.now());

		for (Reservation reservation : reservationList) {
			if (reservation.getItem().getItemStatus() == ItemStatus.PRIVATE) {
				reservation.getItem().setItemStatus(ItemStatus.PUBLIC);
			}
			itemReserveRepository.delete(reservation); // deleteAll
		}
	}
}
