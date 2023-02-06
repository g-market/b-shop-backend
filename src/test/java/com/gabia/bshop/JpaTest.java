/* Licensed under Apache Corp */
package com.gabia.bshop;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.repository.MemberRepository;

@SpringBootTest
public class JpaTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void 유저를_저장한다() {
		// given
		final Member member = Member.builder()
			.hiworksId("hiworksId")
			.email("email")
			.name("Test User")
			.phoneNumber("phoneNumber")
			.role(MemberRole.NORMAL).grade(MemberGrade.BRONZE)
			.build();
		// when
		memberRepository.save(member);
		// then
		assertThat(member.getId()).isEqualTo(1L);
	}
}
