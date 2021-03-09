package com.project.urlshortner.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.urlshortner.model.Url;

import io.lettuce.core.ClientOptions;

@Configuration
public class RedisConfig {

	private final String url;
	private final int port;
	private final String password;

	@Autowired
	private ObjectMapper objectMapper;

	public RedisConfig(@Value("${spring.redis.host}") String url, @Value("${spring.redis.port}") int port,
			@Value("${spring.redis.password}") String password) {
		this.url = url;
		this.port = port;
		this.password = password;
	}

	/**
	 * Redis configuration
	 *
	 * @return redisStandaloneConfiguration
	 */
	@Bean
	public RedisStandaloneConfiguration redisStandaloneConfiguration() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(url, port);
		redisStandaloneConfiguration.setPassword(password);
		return redisStandaloneConfiguration;
	}

	/**
	 * Client Options Reject requests when redis is in disconnected state and Redis
	 * will retry to connect automatically when redis server is down
	 *
	 * @return client options
	 */
	@Bean
	public ClientOptions clientOptions() {
		return ClientOptions.builder().disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
				.autoReconnect(true).build();
	}

	/**
	 * Create a LettuceConnection with redis configurations and client options
	 *
	 * @param redisStandaloneConfiguration redisStandaloneConfiguration
	 * @return RedisConnectionFactory
	 */
	@Bean
	public RedisConnectionFactory connectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {

		LettuceClientConfiguration configuration = LettuceClientConfiguration.builder().clientOptions(clientOptions())
				.build();

		return new LettuceConnectionFactory(redisStandaloneConfiguration, configuration);
	}

	// Setting up the redis template object.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	@Primary
	public RedisTemplate<String, Url> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Url.class);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

		RedisTemplate<String, Url> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;
	}
}
