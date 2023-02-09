package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.MemberDto;
import com.gabia.bshop.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

	MemberDto memberToDto(Member member);
}
