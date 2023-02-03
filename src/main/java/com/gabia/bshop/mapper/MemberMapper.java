package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.MemberDto;
import com.gabia.bshop.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDto memberToDto(Member member);
}
