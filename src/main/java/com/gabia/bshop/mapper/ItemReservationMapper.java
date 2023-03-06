package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.ItemReservationResponse;
import com.gabia.bshop.entity.Reservation;
@Mapper(componentModel = "spring")
public interface ItemReservationMapper {

	ItemReservationMapper INSTANCE = Mappers.getMapper(ItemReservationMapper.class);

	@Mappings({
		@Mapping(source = "item.id", target = "itemId"),
		@Mapping(source = "item.itemStatus", target = "itemStatus"),
		@Mapping(source = "item.openAt", target = "openAt")
	})
	ItemReservationResponse reservationToResponse(Reservation reservation);

}
