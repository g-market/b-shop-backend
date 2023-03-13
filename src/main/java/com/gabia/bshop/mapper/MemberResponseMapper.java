package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.entity.Member;

@Mapper(componentModel = "spring")
public abstract class MemberResponseMapper extends MapperSupporter {

	public static final MemberResponseMapper INSTANCE = Mappers.getMapper(MemberResponseMapper.class);

	@Mapping(target = "profileImageUrl", expression = "java(addPrefixToProfileUlr(member))")
	public abstract MemberResponse memberToMemberResponse(Member member);
}
