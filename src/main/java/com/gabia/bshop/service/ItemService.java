package com.gabia.bshop.service;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {
    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    /*
    상품 조회
    * */
    public ItemDto getItem(final Long id) {
        final Item item =
                itemRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException("아이템이 존재하지 않습니다. id = " + id));

        return ItemMapper.INSTANCE.itemToDto(item);
    }

    /*
    상품 목록 조회
    * */
    //        public List<ItemDto> getListItems(Pageable page)
    //        {
    //            Page<ItemDto>
    //
    //        }

    /*
    상품 생성
    */
    public ItemDto createItem(ItemDto itemDto) {

        Long categoryId = itemDto.categoryDto().id();

        Category category =
                categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

        Item item = ItemMapper.INSTANCE.itemDtoToEntity(itemDto);
        item.addCategory(category);

        return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
    }

    /*
    상품 수정
    * */
    public ItemDto updateItem(ItemDto itemDto) {
        Item item = itemRepository.findById(itemDto.id()).orElseThrow(EntityNotFoundException::new);
        Long categoryId = itemDto.categoryDto().id();

        Category category =
                categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

        item.update(
                itemDto.name(),
                category,
                itemDto.description(),
                itemDto.basePrice(),
                itemDto.itemStatus(),
                itemDto.openAt());

        return ItemMapper.INSTANCE.itemToDto(itemRepository.save(item));
    }
    /*
    상품 삭제
    * */
    public void deleteItem(Long id) {
        // 삭제 시 리턴 타입
        itemRepository.deleteById(id);
    }
}
