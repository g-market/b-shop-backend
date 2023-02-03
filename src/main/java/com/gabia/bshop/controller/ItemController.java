package com.gabia.bshop.controller;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDto> findItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(itemService.findItem(id));
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> PageItem(Pageable pageable) {
        return ResponseEntity.ok().body(itemService.findListItems(pageable));
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
