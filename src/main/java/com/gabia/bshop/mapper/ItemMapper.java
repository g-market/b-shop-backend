package com.gabia.bshop.mapper;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Item;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item itemDtoToEntity(ItemDto itemDto);

    @Mapping(
            target = "categoryDto",
            expression = "java(CategoryMapper.INSTANCE.categoryToDto(item.getCategory()))")
    ItemDto itemToDto(Item item);

    List<ItemDto> toDtoList(List<Item> entityList);
    //List<OrderItemDto> toDtoList(List<OrderItem> entityList);
}
