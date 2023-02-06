package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.response.LoginMemberResponse;
import com.gabia.bshop.dto.response.LoginResponse;
import com.gabia.bshop.dto.response.LoginResult;
import com.gabia.bshop.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginResponseMapper {

    LoginResponseMapper INSTANCE = Mappers.getMapper(LoginResponseMapper.class);

    default LoginResponse from(LoginResult loginResult) {
        final Member member = loginResult.member();
        final LoginMemberResponse loginMemberResponse = LoginMemberResponseMapper.INSTANCE.from(member);
        if (member.getPhoneNumber() == null) {
            return new LoginResponse(loginResult.accessToken(), false, loginMemberResponse);
        }
        return new LoginResponse(loginResult.accessToken(), true, loginMemberResponse);
    }
}
