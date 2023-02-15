package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.ItemOptionChangeRequest;
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

	public ItemOptionResponse findItemOption(final Long optionId) {
		final ItemOption itemOption = findItemOptionById(optionId);

		return ItemOptionMapper.INSTANCE.ItemOptionToResponse(itemOption);
	}

	public List<ItemOptionResponse> findOptionList(final Long itemId) {
		final List<ItemOption> itemOption = itemOptionRepository.findAllByItem_id(itemId);
		if (itemOption.isEmpty())
			throw new NotFoundException(ITEM_OPTION_NOT_FOUND_EXCEPTION);

		return itemOption.stream().map(ItemOptionMapper.INSTANCE::ItemOptionToResponse).toList();
	}

	@Transactional
	public ItemOptionResponse createItemOption(final ItemOptionRequest itemOptionRequest) {
		Item item = itemRepository.findById(itemOptionRequest.itemId()).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemOptionRequest.itemId())
		);

		final ItemOption itemOption = ItemOptionMapper.INSTANCE.ItemOptionRequestToEntity(itemOptionRequest);

		return ItemOptionMapper.INSTANCE.ItemOptionToResponse(itemOptionRepository.save(itemOption));
	}


	/**
	 * 설명(description) 변경
	 * 가격 변경
	 * 재고 변경
	 */
	@Transactional
	public ItemOptionResponse changeItemOption(final ItemOptionChangeRequest itemOptionChangeRequest) {
		final ItemOption itemOption = findItemOptionById(itemOptionChangeRequest.itemOptionId());

		itemOption.update(itemOptionChangeRequest);

		return ItemOptionMapper.INSTANCE.ItemOptionToResponse(itemOptionRepository.save(itemOption));
	}

	@Transactional
	public void deleteItemOption(final Long optionId) {
		final ItemOption itemOption = findItemOptionById(optionId);
		itemOptionRepository.delete(itemOption);
	}

	private ItemOption findItemOptionById(final Long itemOptionId) {
		return itemOptionRepository.findById(itemOptionId)
			.orElseThrow(() -> new NotFoundException(ITEM_OPTION_OUT_OF_STOCK_EXCEPTION, itemOptionId));
	}
}
