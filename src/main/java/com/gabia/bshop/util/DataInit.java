package com.gabia.bshop.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Profile("local")
@RequiredArgsConstructor
@Component
public class DataInit {

	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;
	private final ItemOptionRepository itemOptionRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;

	@PostConstruct
	public void init() {
		Member member1 = Member.builder()
			.name("admin01")
			.email("admin01@gabia.com")
			.hiworksId("admin01")
			.phoneNumber("01000000001")
			.role(MemberRole.ADMIN)
			.grade(MemberGrade.DIAMOND)
			.build();
		Member member2 = Member.builder()
			.name("admin02")
			.email("admin02@gabia.com")
			.hiworksId("admin02")
			.phoneNumber("01000000002")
			.role(MemberRole.ADMIN)
			.grade(MemberGrade.DIAMOND)
			.build();
		Member member3 = Member.builder()
			.name("admin03")
			.email("admin03@gabia.com")
			.hiworksId("admin03")
			.phoneNumber("01000000003")
			.role(MemberRole.ADMIN)
			.grade(MemberGrade.DIAMOND)
			.build();
		Member member4 = Member.builder()
			.name("admin04")
			.email("admin04@gabia.com")
			.hiworksId("admin04")
			.phoneNumber("01000000004")
			.role(MemberRole.ADMIN)
			.grade(MemberGrade.DIAMOND)
			.build();
		Member member5 = Member.builder()
			.name("admin05")
			.email("admin05@gabia.com")
			.hiworksId("admin05")
			.phoneNumber("01000000005")
			.role(MemberRole.ADMIN)
			.grade(MemberGrade.DIAMOND)
			.build();
		Member member6 = Member.builder()
			.name("normal06")
			.email("normal06@gabia.com")
			.hiworksId("normal06")
			.phoneNumber("01000000006")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();
		Member member7 = Member.builder()
			.name("normal07")
			.email("normal07@gabia.com")
			.hiworksId("normal07")
			.phoneNumber("01000000007")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.SILVER)
			.build();
		Member member8 = Member.builder()
			.name("normal08")
			.email("normal08@gabia.com")
			.hiworksId("normal08")
			.phoneNumber("01000000008")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.GOLD)
			.build();
		Member member9 = Member.builder()
			.name("normal09")
			.email("normal09@gabia.com")
			.hiworksId("normal09")
			.phoneNumber("01000000009")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.PLATINUM)
			.build();
		Member member10 = Member.builder()
			.name("normal10")
			.email("normal10@gabia.com")
			.hiworksId("normal10")
			.phoneNumber("01000000010")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.DIAMOND)
			.build();

		memberRepository.saveAll(
			List.of(member1, member2, member3, member4, member5, member6, member7, member8,
				member9, member10));

		Category category1 = Category.builder()
			.name("카테고리1")
			.build();
		Category category2 = Category.builder()
			.name("카테고리2")
			.build();
		Category category3 = Category.builder()
			.name("카테고리3")
			.build();
		Category category4 = Category.builder()
			.name("카테고리4")
			.build();
		Category category5 = Category.builder()
			.name("카테고리5")
			.build();
		Category category6 = Category.builder()
			.name("카테고리6")
			.build();
		Category category7 = Category.builder()
			.name("카테고리7")
			.build();
		Category category8 = Category.builder()
			.name("카테고리8")
			.build();
		Category category9 = Category.builder()
			.name("카테고리9")
			.build();
		Category category10 = Category.builder()
			.name("카테고리10")
			.build();

		categoryRepository.saveAll(
			List.of(category1, category2, category3, category4, category5, category6, category7,
				category8, category9, category10));

		LocalDateTime now = LocalDateTime.now();
		Item item1 = Item.builder()
			.category(category1)
			.name("temp_item_name1")
			.description("temp_item_1_description " + UUID.randomUUID())
			.basePrice(11111)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.deleted(false)
			.build();
		Item item2 = Item.builder()
			.category(category1)
			.name("temp_item_name2")
			.description("temp_item_2_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.deleted(false)
			.build();
		Item item3 = Item.builder()
			.category(category1)
			.name("temp_item_name3")
			.description("temp_item_3_description " + UUID.randomUUID())
			.basePrice(33333)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.deleted(false)
			.build();
		Item item4 = Item.builder()
			.category(category1)
			.name("temp_item_name4")
			.description("temp_item_4_description " + UUID.randomUUID())
			.basePrice(44444)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.deleted(false)
			.build();
		Item item5 = Item.builder()
			.category(category1)
			.name("temp_item_name5")
			.description("temp_item_5_description " + UUID.randomUUID())
			.basePrice(55555)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.deleted(false)
			.build();
		Item item6 = Item.builder()
			.category(category1)
			.name("temp_item_name6")
			.description("temp_item_6_description " + UUID.randomUUID())
			.basePrice(66666)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.deleted(false)
			.build();
		Item item7 = Item.builder()
			.category(category1)
			.name("temp_item_name7")
			.description("temp_item_7_description " + UUID.randomUUID())
			.basePrice(77777)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.deleted(false)
			.build();
		Item item8 = Item.builder()
			.category(category1)
			.name("temp_item_name8")
			.description("temp_item_8_description " + UUID.randomUUID())
			.basePrice(88888)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.deleted(false)
			.build();
		Item item9 = Item.builder()
			.category(category1)
			.name("temp_item_name9")
			.description("temp_item_9_description " + UUID.randomUUID())
			.basePrice(99999)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.deleted(false)
			.build();
		Item item10 = Item.builder()
			.category(category1)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.deleted(true)
			.build();

		itemRepository.saveAll(
			List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10));

		ItemOption itemOption1 = ItemOption.builder()
			.item(item1)
			.description("description")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		ItemOption itemOption2 = ItemOption.builder()
			.item(item2)
			.description("description")
			.optionPrice(1000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption3 = ItemOption.builder()
			.item(item2)
			.description("description")
			.optionPrice(2000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption4 = ItemOption.builder()
			.item(item6)
			.description("description")
			.optionPrice(2000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption5 = ItemOption.builder()
			.item(item6)
			.description("description")
			.optionPrice(1999)
			.stockQuantity(10)
			.build();

		itemOptionRepository.saveAll(List.of(itemOption1, itemOption2, itemOption3, itemOption4, itemOption5));

		ItemImage itemImage1 = ItemImage.builder()
			.item(item1)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage2 = ItemImage.builder()
			.item(item1)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage3 = ItemImage.builder()
			.item(item2)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage4 = ItemImage.builder()
			.item(item2)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage5 = ItemImage.builder()
			.item(item3)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage6 = ItemImage.builder()
			.item(item3)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage7 = ItemImage.builder()
			.item(item4)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage8 = ItemImage.builder()
			.item(item4)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage9 = ItemImage.builder()
			.item(item5)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage10 = ItemImage.builder()
			.item(item5)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage11 = ItemImage.builder()
			.item(item6)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage12 = ItemImage.builder()
			.item(item6)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage13 = ItemImage.builder()
			.item(item7)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage14 = ItemImage.builder()
			.item(item7)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage15 = ItemImage.builder()
			.item(item8)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage16 = ItemImage.builder()
			.item(item8)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage17 = ItemImage.builder()
			.item(item9)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage18 = ItemImage.builder()
			.item(item9)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage19 = ItemImage.builder()
			.item(item10)
			.url(UUID.randomUUID().toString())
			.build();
		ItemImage itemImage20 = ItemImage.builder()
			.item(item10)
			.url(UUID.randomUUID().toString())
			.build();

		itemImageRepository.saveAll(
			List.of(itemImage1, itemImage2, itemImage3, itemImage4, itemImage5, itemImage6,
				itemImage7, itemImage8,
				itemImage9, itemImage10, itemImage11, itemImage12, itemImage13, itemImage14,
				itemImage15, itemImage16,
				itemImage17, itemImage18, itemImage19, itemImage20));

		Order order1 = Order.builder()
			.member(member6)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(11111L)
			.build();
		Order order2 = Order.builder()
			.member(member6)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(33333L)
			.build();
		Order order3 = Order.builder()
			.member(member7)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(22222L)
			.build();

		Order order4 = Order.builder()
			.member(member7)
			.status(OrderStatus.COMPLETED)
			.totalPrice(44444L)
			.build();

		orderRepository.saveAll(List.of(order1, order2, order3, order4));

		OrderItem orderItem1 = OrderItem.builder()
			.item(item1)
			.order(order1)
			.option(itemOption1)
			.orderCount(1)
			.price(11111L)
			.build();
		OrderItem orderItem2 = OrderItem.builder()
			.item(item1)
			.order(order2)
			.option(itemOption1)
			.orderCount(1)
			.price(11111L)
			.build();
		OrderItem orderItem3 = OrderItem.builder()
			.item(item2)
			.order(order2)
			.option(itemOption2)
			.orderCount(1)
			.price(22222L)
			.build();
		OrderItem orderItem4 = OrderItem.builder()
			.item(item2)
			.order(order3)
			.option(itemOption2)
			.orderCount(1)
			.price(22222L)
			.build();
		OrderItem orderItem5 = OrderItem.builder()
			.item(item2)
			.order(order4)
			.option(itemOption2)
			.orderCount(2)
			.price(22222L)
			.build();

		orderItemRepository.saveAll(
			List.of(orderItem1, orderItem2, orderItem3, orderItem4,
				orderItem5));
	}
}
