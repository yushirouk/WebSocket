package com.polling.core.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j // lombok
@Configuration
@Profile("local")
public class EmbeddedRedisConfig {
	
	private static final Logger LOGGER = Logger.getLogger(EmbeddedRedisConfig.class.getName());

	@Value("${spring.redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() throws IOException {
		LOGGER.log(Level.INFO, "redisPort:" + redisPort);
		redisServer = RedisServer.builder().port(redisPort).setting("maxmemory 128M").build();
		
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		if (redisServer != null) {
			redisServer.stop();
		}
	}
}