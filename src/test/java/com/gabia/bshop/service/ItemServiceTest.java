package com.gabia.bshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class ItemServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void beforeEach() {
        //given
        Category category = Category.builder().id(1L).name("name").build();
        Item item1 = Item.builder()
                .id(1L)
                .category(category)
                .name("item")
                .itemStatus(ItemStatus.PUBLIC)
                .basePrice(10000)
                .description("description")
                .deleted(false)
                .openAt(LocalDateTime.now()).build();

        Item item2 = Item.builder()
                .id(2L)
                .category(category)
                .name("item2")
                .itemStatus(ItemStatus.PUBLIC)
                .basePrice(10000)
                .description("description")
                .deleted(false)
                .openAt(LocalDateTime.now()).build();


        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(category));

        //존재하는 상품
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(item2));

        //존재하지 않는 상품
        when(itemRepository.findById(3L)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    void 상품_조회() {
        //then
        assertEquals(1L, itemService.getItem(1L).id());
        assertEquals(2L, itemService.getItem(2L).id());
    }

    @Test
    void 상품_목록_조회() {

    }

    @Test
    void 상품_정보_수정() {

    }

    @Test
    void 상품_제거_성공() {
        itemService.deleteItem(1L);
        itemService.deleteItem(2L);
        verify(itemRepository, times(2)).deleteById(anyLong());
    }
    @Test
    void 상품_제거_실패(){
        Assertions.assertThrows(EntityNotFoundException.class, () ->{
            itemService.deleteItem(3L);
        });
    }

    @Test
    void 상품_재고_변경() {}
}
