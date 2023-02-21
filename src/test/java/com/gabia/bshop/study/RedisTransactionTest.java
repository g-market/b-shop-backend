package com.gabia.bshop.study;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.gabia.bshop.integration.IntegrationTest;

class RedisTransactionTest extends IntegrationTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		final RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
		connectionFactory.getConnection().serverCommands().flushAll();
	}

	@Test
	void multi_exec_test() {
		// given
		stringRedisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(final RedisOperations operations) throws DataAccessException {
				operations.multi(); // 트랜잭션 시작

				operations.opsForValue().set("key1", "value1");
				operations.opsForValue().set("key2", "value2");

				return operations.exec(); // 종료
			}
		});

		final String value1 = stringRedisTemplate.opsForValue().get("key1");
		final String value2 = stringRedisTemplate.opsForValue().get("key2");

		assertThat(value1).isEqualTo("value1");
		assertThat(value2).isEqualTo("value2");
	}

	@Test
	void multi_exec_2() {
		// given : 중간에 비정상적으로 실패하면 데이터 업데이트 적용안됨
		assertThatThrownBy(() -> stringRedisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(final RedisOperations operations) throws DataAccessException {
				operations.multi(); // 트랜잭션 시작

				operations.opsForValue().set("key1", "value1");
				operations.opsForValue().set("key2", "value2");

				if (true) {
					throw new RuntimeException("예외 발생");
				}
				return operations.exec(); // 종료
			}
		})).isInstanceOf(RuntimeException.class);

		final String value1 = stringRedisTemplate.opsForValue().get("key1");
		final String value2 = stringRedisTemplate.opsForValue().get("key2");

		assertThat(value1).isNull();
		assertThat(value2).isNull();
	}

	@Test
	@DisplayName("@transaction stringredistemplate 트랜잭션 테스트")
	void transaction_stringRedisTemplate() {
		assertThatThrownBy(() -> transactionService.stringRedisTemplateCheckRollback()).isInstanceOf(
			RuntimeException.class);

		final String value1 = stringRedisTemplate.opsForValue().get("key11");
		final String value2 = stringRedisTemplate.opsForValue().get("key22");

		assertThat(value1).isNull();
		assertThat(value2).isNull();
	}

	@Test
	@DisplayName("@transaction redisTemplate 트랜잭션 테스트")
	void transaction_redisTemplate() {
		assertThatThrownBy(() -> transactionService.redisTemplateCheckRollback()).isInstanceOf(
			RuntimeException.class);

		final Object value1 = redisTemplate.opsForValue().get("key11");
		final Object value2 = redisTemplate.opsForValue().get("key22");

		assertThat(value1).isNull();
		assertThat(value2).isNull();
	}
}
