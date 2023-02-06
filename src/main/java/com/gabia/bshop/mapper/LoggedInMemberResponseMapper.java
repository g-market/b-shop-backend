package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.LoggedInMemberResponse;
import com.gabia.bshop.entity.Member;

@Mapper(componentModel = "spring")
public interface LoggedInMemberResponseMapper {

	LoggedInMemberResponseMapper INSTANCE = Mappers.getMapper(LoggedInMemberResponseMapper.class);

	LoggedInMemberResponse from(Member member);
}
