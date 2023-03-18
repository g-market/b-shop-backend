package com.gabia.bshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.request.CategoryCreateRequest;
import com.gabia.bshop.dto.response.CategoryAllInfoResponse;
import com.gabia.bshop.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	CategoryDto categoryToDto(Category category);

	CategoryAllInfoResponse categoryToCategoryAllInfoResponse(Category category);

	@Mapping(target = "id", ignore = true)
	Category CategoryRequestToEntity(CategoryCreateRequest categoryCreateRequest);
}
