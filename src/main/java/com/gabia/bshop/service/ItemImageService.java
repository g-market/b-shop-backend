package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.request.ItemImageCreateRequest;
import com.gabia.bshop.dto.request.ItemThumbnailChangeRequest;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemImageMapper;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.util.ImageValidate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemImageService {
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	private final ImageValidate imageValidate;

	public ItemImageDto findItemImage(final Long imageId) {
		final ItemImage itemImage = findItemImageById(imageId);
		return ItemImageMapper.INSTANCE.itemImageToDto(itemImage);
	}

	public List<ItemImageDto> findItemImageLists(final Long itemId) {
		final List<ItemImage> itemImageList = itemImageRepository.findAllByItem_id(itemId);
		return itemImageList.stream().map(ItemImageMapper.INSTANCE::itemImageToDto).toList();
	}

	@Transactional
	public List<ItemImageDto> createItemImage(final ItemImageCreateRequest itemImageCreateRequest) {
		final Item item = findItemById(itemImageCreateRequest.itemId());

		List<ItemImage> itemImageList = new ArrayList<>();

		for (String imageUrl : itemImageCreateRequest.urlList()) {
			urlValidate(imageUrl); //image validate
			itemImageList.add(ItemImage.builder().item(item).url(imageUrl).build());
		}
		itemImageList = itemImageRepository.saveAll(itemImageList);

		return itemImageList.stream().map(ItemImageMapper.INSTANCE::itemImageToDto).toList();
	}

	@Transactional
	public ItemImageDto changeItemImage(final ItemImageDto itemImageDto) {
		ItemImage itemImage = findItemImageById(itemImageDto.id());
		urlValidate(itemImageDto.url()); //image validate
		itemImage.updateUrl(itemImageDto.url());
		return ItemImageMapper.INSTANCE.itemImageToDto(itemImageRepository.save(itemImage));
	}

	@Transactional
	public ItemResponse changeItemThumbnail(final ItemThumbnailChangeRequest itemThumbnailChangeRequest) {
		Item item = findItemById(itemThumbnailChangeRequest.itemId());
		final ItemImage itemImage = findItemImageById(itemThumbnailChangeRequest.imageId());

		urlValidate(itemImage.getUrl()); // image validate
		item.setThumbnail(itemImage);

		return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(item));
	}

	@Transactional
	public void deleteItemImage(final Long imageId) {
		final ItemImage itemImage = findItemImageById(imageId);
		itemImageRepository.delete(itemImage);
	}

	private ItemImage findItemImageById(final Long imageId) {
		return itemImageRepository.findById(imageId).orElseThrow(
			() -> new NotFoundException(IMAGE_NOT_FOUND_EXCEPTION, imageId)
		);
	}

	private Item findItemById(final Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId));
	}

	private void urlValidate(final String url) {
		final boolean isValid = imageValidate.validate(url);

		if (!isValid) {
			throw new NotFoundException(INCORRECT_URL_EXCEPTION);
		}
	}
}
