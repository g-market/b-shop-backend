package com.gabia.bshop.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.gabia.bshop.config.ImageDefaultProperties;
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
	private final ImageDefaultProperties imageDefaultProperties;

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

		Category kitchen = Category.builder()
			.name("주방")
			.build();
		Category living = Category.builder()
			.name("리빙")
			.build();
		Category interior = Category.builder()
			.name("인테리어")
			.build();
		Category horticulture = Category.builder()
			.name("원예")
			.build();
		Category health = Category.builder()
			.name("건강")
			.build();
		Category beauty = Category.builder()
			.name("미용")
			.build();
		Category fashion = Category.builder()
			.name("패션")
			.build();
		Category stationery = Category.builder()
			.name("문구")
			.build();
		Category party = Category.builder()
			.name("파티")
			.build();
		Category packaging = Category.builder()
			.name("포장")
			.build();
		Category mobile = Category.builder()
			.name("모바일")
			.build();
		Category sports = Category.builder()
			.name("스포츠")
			.build();
		Category hobby = Category.builder()
			.name("취미")
			.build();
		Category toy = Category.builder()
			.name("완구")
			.build();
		Category companionAnimal = Category.builder()
			.name("반려동물")
			.build();
		Category food = Category.builder()
			.name("식품")
			.build();

		categoryRepository.saveAll(
			List.of(kitchen, living, interior, horticulture, health, beauty, fashion, stationery, party, packaging,
				mobile, sports, hobby, toy, companionAnimal, food));

		LocalDateTime now = LocalDateTime.now();
		Item item1 = Item.builder()
			.category(kitchen)
			.name("가비아 클라우드 백")
			.description(
				"![img1](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/b359bb5642a5a3461edf9f634ce3915d/%E1%84%80%E1%85%B3%E1%84%85%E1%85%AE%E1%86%B8-186119-1100x1100.png)\n"
					+ "\n"
					+ "\n"
					+ "## **GABIA CLOUD BAG\\_2022**\n"
					+ "\n"
					+ "\n"
					+ "**가비아 클라우드 전시를 위한 리유저블백\n"
					+ "장바구니 제작 요청**\n"
					+ "\n"
					+ "전시에서 들고 다녀 눈에 띄고 홍보가 될 것, 전시장을 나가서도 사용할 수 있을 것\n"
					+ "\n"
					+ "<br/>\n"
					+ "\n"
					+ "가비아 클라우드/IT 전시에서 사용될 가비아 클라우드 가방이 필요하다는 요청이 디자인팀에 전달되었습니다.\n"
					+ "\n"
					+ "전시장 뿐만 아니라 일상에서도 사용가능한 리유저블 가방을 목표로 진행하였는데요,\n"
					+ "\n"
					+ "다양한 시도끝에 제작된 가비아 클라우드 가방을 소개합니다.\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "시작은 전시회에서 받은 가방을 마트에서도 사용할 수 있는 장바구니의 형태와 폴딩형태에 중점을 두고 진행하였습니다.\n"
					+ "\n"
					+ "다양한 형태의 장바구니를 리서치하고, 샘플을 모으고, 실제 일상에서 사용하는 제품들을 찾아보았습니다.\n"
					+ "\n"
					+ "폴딩 형태의 바쿠백이 가장 대표적인 그래픽이 가미된 리유저블백으로 실제로도 많은 사람들이 이용하고 있었습니다.\n"
					+ "\n"
					+ "우선 바쿠백 형태로 그래픽 작업을 진행하였습니다.\n"
					+ "\n"
					+ "![img2](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/66f054faae5376d8d7b5603987112648/image.png)![img3](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/2b408e21b69faf7b6492dcde8a4a942e/image.png)\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "다양한 그래픽을 적용한 끝에 임직원들의 의견을 토대로 가비아 브랜드에 적합한 디자인을 선별하였습니다.\n"
					+ "\n"
					+ "바쿠백 뿐만 아니라 크게 3가지 종류의 장바구니 형태로 나누어 가비아 그래픽을 적용해 보고,\n"
					+ "\n"
					+ "이를 토대로 장바구니 사용과 전시에 관한 인터뷰를 진행하였습니다.\n"
					+ "\n"
					+ "![그룹 189395](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/eb506e9fc73d00f4875b0b173a9ab7ad/image.png)\n"
					+ "\n"
					+ "![그룹 189397](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/d32666e83aef0696db2800e232303767/image.png)\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "클라우드/IT 전시회 관련부서 사내직원분들과 인터뷰를 진행하며 다양한 의견을 들을 수 있었습니다.\n"
					+ "\n"
					+ "우선 타겟층인 3040남성분들은 장바구니를 쓰지 않는다는 의견이 지배적이었고, 장바구니에 대한 선호도가 낮은것으로 마무리하였습니다.\n"
					+ "\n"
					+ "하지만 전시회에서 가비아의 리플렛과 소개서 등을 넣어줄 가방이 필요하다는 사실은 변함이 없었고,\n"
					+ "\n"
					+ "튼튼한 내구성을 갖춘 쇼핑백 형태의 가방을 선호한다는 것을 알 수 있었습니다.\n"
					+ "\n"
					+ "\n"
					+ "![그룹 189400](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/b464f0c62baa266d65184aa1e2ee4539/image.png)\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "그리하여 리서치와 인터뷰 도중에인기가 많았던 리유저블백을 토대로 제품을 특정하였고,\n"
					+ "\n"
					+ "흐믈거리는 바쿠백 보다는 탄탄한 느낌의  R-PET 리유저블백으로 방향을 전환하였습니다.\n"
					+ "\n"
					+ "실제로 길거리에서 나이키 브랜드의 리유저블백을 사용하는 사람들을 종종 볼 수 있고,\n"
					+ "\n"
					+ "전시회에서 요구하는 튼튼한 제품에 적합한 소재였습니다.\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "![그룹 189404](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/1c2e1fbb74f1b875ab17d47b54c63418/image.png)\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "다양한 업체에 컨택하였고, 여러 레퍼런스를 갖춘 업체에 방문하여 상담 후, 세가지 시안으로 샘플을 제작하였습니다.\n"
					+ "\n"
					+ "클라우드를 상징하는 큐브형태의 그래픽 디자인과 가비아의  g.의 형태로 이루어진 그래픽 작업을 진행하였고,\n"
					+ "\n"
					+ "내부적으로 목업을 제작해하며 사이즈와 디자인 등을 추려 3가지 디자인의 샘플을 의뢰하였습니다.\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "![그룹 186123](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/e7b4edf58962338ed4b956429c23c1dc/%E1%84%80%E1%85%B3%E1%84%85%E1%85%AE%E1%86%B8-186123.png)\n"
					+ "\n"
					+ "![그룹 186122](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/7953901ec2783b255271716062e0a6bd/%E1%84%80%E1%85%B3%E1%84%85%E1%85%AE%E1%86%B8-186122.png)\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "3종 샘플 제작 후, 사내 인터뷰 및 선호도 조사를 통해 최종 1번 시안으로 진행하기로 하였습니다.\n"
					+ "\n"
					+ "샘플에서 손잡이의 길이와 위치 등 세부적인 디테일을 조정하여 최종적으로 가비아 클라우드백이 완성되었습니다.\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "![마스크 그룹 136](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/6b34fdfab533a5a14d18b756fab770a5/%E1%84%86%E1%85%A1%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3-%E1%84%80%E1%85%B3%E1%84%85%E1%85%AE%E1%86%B8-136.png)\n"
					+ "\n"
					+ "![그룹 189405](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/uploads/54594c14defea667412f2919e336fa20/%E1%84%80%E1%85%B3%E1%84%85%E1%85%AE%E1%86%B8-189405.png)\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "블랙&화이트 컬러로 어디에나 잘 어울릴 수 있는 가비아 클라우드 가방이 완성되었습니다.\n"
					+ "\n"
					+ "코로나로 인해 아직 전시/컨퍼런스에 대한 계획이 없지만 조만간 전시장에서 만나보기를 기대합니다.\n"
					+ "\n"
					+ "R-PET라는 재활용 소재로 제작되어 클라우드 환경뿐만 아니라 지구환경까지(!) 생각하는 마음을 담긴 가비아 클라우드 가방 소개를 마치겠습니다.\n")
			.basePrice(10000)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item2 = Item.builder()
			.category(kitchen)
			.name("temp_item_name2")
			.description("temp_item_2_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2022)
			.build();
		Item item3 = Item.builder()
			.category(kitchen)
			.name("temp_item_name3")
			.description("temp_item_3_description " + UUID.randomUUID())
			.basePrice(33333)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2022)
			.build();
		Item item4 = Item.builder()
			.category(kitchen)
			.name("temp_item_name4")
			.description("temp_item_4_description " + UUID.randomUUID())
			.basePrice(44444)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item5 = Item.builder()
			.category(kitchen)
			.name("temp_item_name5")
			.description("temp_item_5_description " + UUID.randomUUID())
			.basePrice(55555)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item6 = Item.builder()
			.category(kitchen)
			.name("temp_item_name6")
			.description("temp_item_6_description " + UUID.randomUUID())
			.basePrice(66666)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item7 = Item.builder()
			.category(kitchen)
			.name("temp_item_name7")
			.description("temp_item_7_description " + UUID.randomUUID())
			.basePrice(77777)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item8 = Item.builder()
			.category(kitchen)
			.name("temp_item_name8")
			.description("temp_item_8_description " + UUID.randomUUID())
			.basePrice(88888)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item9 = Item.builder()
			.category(kitchen)
			.name("temp_item_name9")
			.description("temp_item_9_description " + UUID.randomUUID())
			.basePrice(99999)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item10 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true

		Item item11 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true
		Item item12 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true
		Item item13 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted trueItem item10 = Item.builder()
		Item item14 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true

		Item item15 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true
		Item item16 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true

		Item item17 = Item.builder()
			.category(kitchen)
			.name("temp_item_name4")
			.description("temp_item_4_description " + UUID.randomUUID())
			.basePrice(44444)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item18 = Item.builder()
			.category(kitchen)
			.name("temp_item_name5")
			.description("temp_item_5_description " + UUID.randomUUID())
			.basePrice(55555)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item19 = Item.builder()
			.category(kitchen)
			.name("temp_item_name6")
			.description("temp_item_6_description " + UUID.randomUUID())
			.basePrice(66666)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item20 = Item.builder()
			.category(kitchen)
			.name("temp_item_name7")
			.description("temp_item_7_description " + UUID.randomUUID())
			.basePrice(77777)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item21 = Item.builder()
			.category(kitchen)
			.name("temp_item_name8")
			.description("temp_item_8_description " + UUID.randomUUID())
			.basePrice(88888)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item22 = Item.builder()
			.category(kitchen)
			.name("temp_item_name9")
			.description("temp_item_9_description " + UUID.randomUUID())
			.basePrice(99999)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item23 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true

		Item item24 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true
		Item item25 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted true
		Item item26 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.openAt(now)
			.build();//deleted trueItem item10 = Item.builder()
		Item item27 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.year(2023)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.openAt(now)
			.build();//deleted true
		Item item28 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.year(2023)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.openAt(now)
			.build();//deleted true
		Item item29 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.year(2023)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.openAt(now)
			.build();//deleted true

		Item item30 = Item.builder()
			.category(kitchen)
			.name("temp_item_name10")
			.description("temp_item_10_description " + UUID.randomUUID())
			.basePrice(12345)
			.itemStatus(ItemStatus.PUBLIC)
			.year(2023)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.openAt(now)
			.build();//deleted true
		Item item31 = Item.builder()
			.category(kitchen)
			.name("temp_item_name4")
			.description("temp_item_4_description " + UUID.randomUUID())
			.basePrice(44444)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item32 = Item.builder()
			.category(kitchen)
			.name("temp_item_name5")
			.description("temp_item_5_description " + UUID.randomUUID())
			.basePrice(55555)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item33 = Item.builder()
			.category(kitchen)
			.name("temp_item_name6")
			.description("temp_item_6_description " + UUID.randomUUID())
			.basePrice(66666)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item34 = Item.builder()
			.category(kitchen)
			.name("temp_item_name7")
			.description("temp_item_7_description " + UUID.randomUUID())
			.basePrice(77777)
			.itemStatus(ItemStatus.PRIVATE)
			.openAt(now.minusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item35 = Item.builder()
			.category(kitchen)
			.name("temp_item_name8")
			.description("temp_item_8_description " + UUID.randomUUID())
			.basePrice(88888)
			.itemStatus(ItemStatus.RESERVED)
			.openAt(now.plusDays(1L))
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();

		itemRepository.saveAll(
			List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10,
				item11, item12, item13, item14, item15, item16, item17, item18, item19, item20,
				item21, item22, item23, item24, item25, item26, item27, item28, item29, item30,
				item31, item32, item33, item34, item35));

		ItemOption itemOption1 = ItemOption.builder()
			.item(item1)
			.description("itemOptionDescription")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		ItemOption itemOption2 = ItemOption.builder()
			.item(item1)
			.description("itemOptionDescription")
			.optionPrice(1000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption3 = ItemOption.builder()
			.item(item1)
			.description("itemOptionDescription")
			.optionPrice(2000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption4 = ItemOption.builder()
			.item(item1)
			.description("itemOptionDescription")
			.optionPrice(2000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption5 = ItemOption.builder()
			.item(item1)
			.description("itemOptionDescription")
			.optionPrice(1999)
			.stockQuantity(10)
			.build();

		ItemOption itemOption6 = ItemOption.builder()
			.item(item4)
			.description("xl")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		ItemOption itemOption7 = ItemOption.builder()
			.item(item4)
			.description("xs")
			.optionPrice(1000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption8 = ItemOption.builder()
			.item(item4)
			.description("s")
			.optionPrice(2000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption9 = ItemOption.builder()
			.item(item4)
			.description("m")
			.optionPrice(2000)
			.stockQuantity(5)
			.build();

		ItemOption itemOption10 = ItemOption.builder()
			.item(item4)
			.description("l")
			.optionPrice(1999)
			.stockQuantity(10)
			.build();

		itemOptionRepository.saveAll(List.of(itemOption1, itemOption2, itemOption3, itemOption4, itemOption5,
			itemOption6, itemOption7, itemOption8, itemOption9, itemOption10));

		ItemImage itemImage1 = ItemImage.builder()
			.item(item1)
			.url("http://127.0.0.1:9000/images/1.png")
			.build();
		ItemImage itemImage2 = ItemImage.builder()
			.item(item1)
			.url("http://127.0.0.1:9000/images/2.png")
			.build();
		ItemImage itemImage3 = ItemImage.builder()
			.item(item1)
			.url("http://127.0.0.1:9000/images/3.png")
			.build();
		ItemImage itemImage4 = ItemImage.builder()
			.item(item2)
			.url("http://127.0.0.1:9000/images/1.png")
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
