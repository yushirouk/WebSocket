package com.polling.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.TestPropertySource;

@EnableAutoConfiguration
@SpringBootTest
@TestPropertySource("classpath:application-local.properties")
public class RedisRepositoryConfigTest {
	
	@Autowired
	StringRedisTemplate redisTemplate;
	
	@Test
	public void testString() {
		final String key = "testString";
		final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
		stringStringValueOperations.set(key, "1");
		
		final String result_1 = stringStringValueOperations.get(key);
		
		assertEquals(result_1, "1");
	}
}
