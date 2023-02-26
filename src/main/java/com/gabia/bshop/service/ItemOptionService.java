package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.response.ItemOptionResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemOptionMapper;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemOptionService {
	private final ItemRepository itemRepository;
	private final ItemOptionRepository itemOptionRepository;

	public ItemOptionResponse findItemOption(final Long itemId, final Long optionId) {
		final ItemOption itemOption = findItemOptionByItemIdAndOptionId(itemId, optionId);

		return ItemOptionMapper.INSTANCE.itemOptionToResponse(itemOption);
	}

	public List<ItemOptionResponse> findOptionList(final Long itemId) {
		final List<ItemOption> itemOptionList = itemOptionRepository.findAllByItem_id(itemId);
		if (itemOptionList.isEmpty()) {
			throw new NotFoundException(ITEM_OPTION_NOT_FOUND_EXCEPTION);
		}
		return itemOptionList.stream().map(ItemOptionMapper.INSTANCE::itemOptionToResponse).toList();
	}

	@Transactional
	public ItemOptionResponse createItemOption(final Long itemId, final ItemOptionRequest itemOptionRequest) {
		Item item = itemRepository.findById(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId)
		);

		final ItemOption itemOption = ItemOptionMapper.INSTANCE.itemOptionRequestToEntity(itemOptionRequest);

		itemOption.update(item);

		return ItemOptionMapper.INSTANCE.itemOptionToResponse(itemOptionRepository.save(itemOption));
	}

	/**
	 * 설명(description) 변경
	 * 가격 변경
	 * 재고 변경
	 */
	@Transactional
	public ItemOptionResponse changeItemOption(
		final Long itemId,
		final Long optionId,
		final ItemOptionRequest itemOptionRequest) {

		final ItemOption itemOption = findItemOptionByItemIdAndOptionIdWithLock(itemId, optionId);

		itemOption.update(itemOptionRequest);

		return ItemOptionMapper.INSTANCE.itemOptionToResponse(itemOption);
	}

	@Transactional
	public void deleteItemOption(final Long itemId, final Long optionId) {
		final ItemOption itemOption = findItemOptionByItemIdAndOptionId(itemId, optionId);
		itemOptionRepository.delete(itemOption);
	}

	private ItemOption findItemOptionByItemIdAndOptionId(final Long itemId, final Long itemOptionId) {
		return itemOptionRepository.findByIdAndItemId(itemOptionId, itemId)
			.orElseThrow(() -> new NotFoundException(ITEM_OPTION_NOT_FOUND_EXCEPTION, itemId, itemOptionId));
	}

	private ItemOption findItemOptionByItemIdAndOptionIdWithLock(final Long itemId, final Long itemOptionId) {
		return itemOptionRepository.findByIdAndItemIdWithLock(itemOptionId, itemId)
			.orElseThrow(() -> new NotFoundException(ITEM_OPTION_NOT_FOUND_EXCEPTION, itemId, itemOptionId));
	}
}
