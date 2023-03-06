package com.gabia.bshop.schedule;

import static com.gabia.bshop.entity.enumtype.ItemStatus.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.repository.ReservationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationUpdateScheduler {
	private final ReservationRepository reservationRepository;

	@Scheduled(cron = "0 * * * * *") // 1분 마다 실행
	@SchedulerLock(
		name = "scheduledItemStatusUpdateTask",
		lockAtLeastFor = "10s", // 최소 잠금 시간
		lockAtMostFor = "PT10M") // 최대 잠금 시간 10분
	@Transactional
	public void updateReservationStatus() {

		final List<Reservation> reservationList = reservationRepository.findAllByItemOpenAtBefore(LocalDateTime.now());
		final List<Long> removeReservationIdList = new ArrayList<>();
		final List<Long> updateItemIdList = new ArrayList<>();

		for (final Reservation reservation : reservationList) {
			if (reservation.getItem().getItemStatus() == RESERVED) {
				updateItemIdList.add(reservation.getItem().getId());
			}
			removeReservationIdList.add(reservation.getId());
		}
		reservationRepository.updateAllItemStatusAndDeleteReservation(removeReservationIdList, updateItemIdList,
			PUBLIC);
	}
}
