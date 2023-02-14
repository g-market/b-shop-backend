package com.gabia.bshop.study;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.gabia.bshop.integration.IntegrationTest;

@DisplayName("레디스 메서드 테스트")
public class RedisStudyTest extends IntegrationTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	@DisplayName("string 테스트")
	void string() {
		// given
		final ValueOperations<String, Object> string = redisTemplate.opsForValue();

		// when
		string.set("member:name", "jaime");
		string.set("member:id", "1");

		final Object name = string.get("member:name");
		final Object memberId = string.get("member:id");

		// then
		assertThat(name).isEqualTo("jaime");
		assertThat(memberId).isEqualTo("1");
	}

	@Test
	@DisplayName("list 테스트")
	void list() {
		// given
		final ListOperations<String, Object> list = redisTemplate.opsForList();
		final String key = "memberList";
		final List<Object> expect = List.of("member1", "member2", "member3", "member4");

		// when
		list.rightPushAll(key, expect);
		final List<Object> range = list.range(key, 0, 3);

		// then
		assertThat(range).usingRecursiveComparison().isEqualTo(expect);
	}

	@Test
	@DisplayName("set 테스트")
	void set() {
		// given
		final SetOperations<String, Object> set = redisTemplate.opsForSet();
		final String key = "memberSet";
		final Set<Object> expect = Set.of("member1", "member2", "member3", "member4");

		// when
		expect.forEach(obj -> set.add(key, obj));
		final Set<Object> members = set.members(key);

		// then
		assertThat(members).usingRecursiveComparison().isEqualTo(expect);
	}

	@Test
	@DisplayName("sortedSet 테스트")
	void sortedSet() {
		// given
		final ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
		final String key = "userRank";
		final Set<Object> expect = Set.of("member1", "member2", "member3", "member4");

		// when
		zSet.add(key, "member1", 3);
		zSet.add(key, "member2", 4);
		zSet.add(key, "member3", 1);
		zSet.add(key, "member4", 2);

		final Set<Object> range = zSet.range(key, 0, 4);

		// then
		assertAll(
			() -> assertThat(range.size()).isEqualTo(4),
			() -> assertThat(range).usingRecursiveComparison().isEqualTo(expect)
		);
	}

	@Test
	@DisplayName("hash 테스트")
	void hash() {
		// given
		final String key = "jaime";
		final HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
		final Set<Object> expect = Set.of("member1", "member2", "member3", "member4");

		// when
		hash.put(key, "id", "1");
		hash.put(key, "age", "27");
		hash.put(key, "name", "jaime");

		final Map<Object, Object> entries = hash.entries(key);

		// then
		assertAll(
			() -> assertThat(entries.get("id")).isEqualTo("1"),
			() -> assertThat(entries.get("age")).isEqualTo("27"),
			() -> assertThat(entries.get("name")).isEqualTo("jaime")
		);
	}
}
