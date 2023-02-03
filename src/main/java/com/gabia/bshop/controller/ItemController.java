package com.gabia.bshop.controller;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(itemService.getItem(id));
    }

    @GetMapping("/items")
            public ResponseEntity<List<ItemDto>> PageItem(Pageable pageable) {
                return ResponseEntity.ok().body(itemService.getListItems(pageable));
    }

    @PostMapping("/items")
    public ResponseEntity<ItemDto> creatItem(@RequestBody ItemDto itemDto) {
        return ResponseEntity.ok().body(itemService.createItem(itemDto));
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto) {
        return ResponseEntity.ok().body(itemService.updateItem(itemDto));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
