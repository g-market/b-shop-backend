package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.LoginMemberResponse;
import com.gabia.bshop.entity.Member;

@Mapper(componentModel = "spring")
public interface LoginMemberResponseMapper {

	LoginMemberResponseMapper INSTANCE = Mappers.getMapper(LoginMemberResponseMapper.class);

	LoginMemberResponse from(Member member);
}
