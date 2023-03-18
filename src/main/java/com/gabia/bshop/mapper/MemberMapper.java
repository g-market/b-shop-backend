package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.entity.Member;

@Mapper(componentModel = "spring")
public abstract class MemberMapper extends MapperSupporter {

	public static final MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

	@Mapping(target = "profileImageUrl", expression = "java(addPrefixToProfileUrl(member))")
	public abstract MemberResponse memberToMemberResponse(Member member);
}
