package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.response.LoggedInMemberResponse;
import com.gabia.bshop.dto.response.LoginMemberResponse;
import com.gabia.bshop.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoggedInMemberResponseMapper {

    LoggedInMemberResponseMapper INSTANCE = Mappers.getMapper(LoggedInMemberResponseMapper.class);

    LoggedInMemberResponse from(Member member);
}
