package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category CategoryDtoToEntity(CategoryDto categoryDto);

    CategoryDto categoryToDto(Category category);
}
