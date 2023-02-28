package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryRequest;
import com.gabia.bshop.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	@Mapping(target = "name", ignore = true)
	Category categoryDtoToEntity(CategoryDto categoryDto);

	CategoryDto categoryToDto(Category category);

	@Mapping(target = "id", ignore = true)
	Category CategoryRequestToEntity(CategoryRequest categoryRequest);
}
