package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import com.gabia.bshop.dto.response.ItemReservationResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemReservationMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.ItemReserveRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemReserveService {
	private final ItemReserveRepository itemReserveRepository;
	private final ItemRepository itemRepository;

	public ItemReservationResponse findItemReservation(final Long itemId) {
		return ItemReservationMapper.INSTANCE.reservationToResponse(findReservationByItemId(itemId));
	}

	public ItemReservationResponse createItemReservation(final Long itemId) {
		final Item item = itemRepository.findById(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId)
		);

		final Reservation reservation = Reservation.builder().item(item).build();
		return ItemReservationMapper.INSTANCE.reservationToResponse(itemReserveRepository.save(reservation));
	}

	public void removeReservation(final Long itemId) {
		final Reservation reservation = findReservationByItemId(itemId);
		itemReserveRepository.delete(reservation);
	}

	private Reservation findReservationByItemId(final Long itemId) {
		return itemReserveRepository.findByItem_Id(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_RESERVATION_NOT_FOUND_EXCEPTION, itemId)
		);
	}

	//https://beaniejoy.tistory.com/56
	@Scheduled(cron = "* * * * * *") //1분에 1번씩 실행
	@SchedulerLock(
		name = "scheduledItemStatusUpdateTask",
		lockAtLeastFor = "10s",
		lockAtMostFor = "15s")
	public void schedulingReservationUpdate() {
		log.info("triggered");
		// final List<Reservation> reservationList = itemReserveRepository.findAllByItemOpenAtBefore(LocalDateTime.now());
		//
		// for(Reservation reservation : reservationList) {
		// 	if(reservation.getItem().getItemStatus() == ItemStatus.RESERVED){
		// 		reservation.getItem().setItemStatus(ItemStatus.PUBLIC);
		// 	}
		// 	itemReserveRepository.delete(reservation);
		// }
	}
}
