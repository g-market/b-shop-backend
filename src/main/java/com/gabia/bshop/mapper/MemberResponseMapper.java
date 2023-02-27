package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberResponseMapper {

	MemberResponseMapper INSTANCE = Mappers.getMapper(MemberResponseMapper.class);

	MemberResponse from(Member member);
}
