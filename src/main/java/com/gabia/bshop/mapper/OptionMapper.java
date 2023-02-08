package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.OptionDto;
import com.gabia.bshop.entity.Options;

@Mapper(componentModel = "spring")
public interface OptionMapper {
	OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

	Options OptionsDtoToEntity(OptionDto optionDto);

	OptionDto OptionsToDto(Options options);
}
