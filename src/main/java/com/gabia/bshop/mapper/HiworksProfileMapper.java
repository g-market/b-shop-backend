package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;

@Mapper(componentModel = "spring")
public interface HiworksProfileMapper {

	HiworksProfileMapper INSTANCE = Mappers.getMapper(HiworksProfileMapper.class);

	default Member toEntity(HiworksProfileResponse hiworksProfileResponse) {
		return Member.builder()
			.email(hiworksProfileResponse.email())
			.name(hiworksProfileResponse.name())
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.hiworksId(hiworksProfileResponse.hiworksId())
			.build();
	}
}
