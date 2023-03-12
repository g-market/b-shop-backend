package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.ItemImageCreateRequest;
import com.gabia.bshop.dto.request.ItemImageUpdateRequest;
import com.gabia.bshop.dto.request.ItemThumbnailUpdateRequest;
import com.gabia.bshop.dto.response.ItemImageResponse;
import com.gabia.bshop.dto.response.ItemResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.exception.BadRequestException;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemImageMapper;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.util.ImageValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemImageService {

	private static final int MAX_ITEM_IMAGE_COUNT = 100;

	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;
	private final ImageValidator imageValidator;

	public ItemImageResponse findItemImage(final Long itemId, final Long imageId) {
		final ItemImage itemImage = findItemImageByImageIdAndItemId(imageId, itemId);
		return ItemImageMapper.INSTANCE.itemImageToItemImageResponse(itemImage);
	}

	public List<ItemImageResponse> findItemImageList(final Long itemId) {
		final List<ItemImage> itemImageList = itemImageRepository.findAllByItemId(itemId);
		return itemImageList.stream().map(ItemImageMapper.INSTANCE::itemImageToItemImageResponse).toList();
	}

	@Transactional
	public List<ItemImageResponse> createItemImage(final Long itemId,
		final ItemImageCreateRequest itemImageCreateRequest) {
		final Item item = findItemById(itemId);

		List<ItemImage> itemImageList = new ArrayList<>();
		final int totalImageSize = item.getItemImageList().size() + itemImageCreateRequest.urlList().size();

		if (totalImageSize > MAX_ITEM_IMAGE_COUNT) {
			throw new ConflictException(MAX_ITEM_IMAGE_LIMITATION_EXCEPTION, MAX_ITEM_IMAGE_COUNT);
		}

		for (String imageUrl : itemImageCreateRequest.urlList()) {
			urlValidate(imageUrl);
			final String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
			itemImageList.add(ItemImage.builder().item(item).imageName(imageName).build());
		}
		itemImageList = itemImageRepository.saveAll(itemImageList);
		return itemImageList.stream().map(ItemImageMapper.INSTANCE::itemImageToItemImageResponse).toList();
	}

	@Transactional
	public ItemImageResponse updateItemImage(final Long itemId, final ItemImageUpdateRequest itemImageUpdateRequest) {
		ItemImage itemImage = findItemImageByImageIdAndItemId(itemImageUpdateRequest.imageId(), itemId);
		urlValidate(itemImageUpdateRequest.imageUrl());

		final String imageName = itemImageUpdateRequest.imageUrl()
			.substring(itemImageUpdateRequest.imageUrl().lastIndexOf("/") + 1);

		itemImage.updateImageName(imageName);

		return ItemImageMapper.INSTANCE.itemImageToItemImageResponse(itemImage);
	}

	@Transactional
	public ItemResponse updateItemThumbnail(final Long itemId,
		final ItemThumbnailUpdateRequest itemThumbnailUpdateRequest) {
		Item item = findItemById(itemId);
		final ItemImage itemImage = findItemImageByImageIdAndItemId(itemThumbnailUpdateRequest.imageId(), itemId);

		urlValidate(itemImage.getImageName()); // image validate
		item.updateThumbnail(itemImage.getImageName());

		return ItemMapper.INSTANCE.itemToItemResponse(item);
	}

	@Transactional
	public void deleteItemImage(final Long itemId, final Long imageId) {
		final ItemImage itemImage = findItemImageByImageIdAndItemId(imageId, itemId);
		itemImageRepository.delete(itemImage);
	}

	private ItemImage findItemImageByImageIdAndItemId(final Long imageId, final Long itemId) {
		return itemImageRepository.findByIdAndItemId(imageId, itemId).orElseThrow(
			() -> new NotFoundException(IMAGE_NOT_FOUND_EXCEPTION, imageId)
		);
	}

	private Item findItemById(final Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId));
	}

	private void urlValidate(final String url) {
		final boolean isValid = imageValidator.validate(url);

		if (!isValid) {
			throw new BadRequestException(INCORRECT_URL_EXCEPTION);
		}
	}
}
