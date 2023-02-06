package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.response.LoginMemberResponse;
import com.gabia.bshop.dto.response.LoginResponse;
import com.gabia.bshop.dto.response.LoginResult;
import com.gabia.bshop.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginMemberResponseMapper {

    LoginMemberResponseMapper INSTANCE = Mappers.getMapper(LoginMemberResponseMapper.class);

    LoginMemberResponse from(Member member);
}
