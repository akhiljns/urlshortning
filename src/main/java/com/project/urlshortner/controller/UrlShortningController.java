package com.project.urlshortner.controller;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.urlshortner.model.Url;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/url")
public class UrlShortningController {

	@Autowired
	private RedisTemplate<String, Url> redisTemplate;

	@Value("${redis.ttl}")
	private long ttl;

	@SuppressWarnings("rawtypes")
	@PostMapping
	public ResponseEntity create(@RequestBody final String url) {
//        final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
//        if (!urlValidator.isValid(url)) {
//			return ResponseEntity.badRequest().body(new Error("Invalid URL."));
//        }

		final Url urlOb = Url.createUsingMurmur(url);
		log.info("URL id generated = {}", urlOb.getId());
		redisTemplate.opsForValue().set(urlOb.getId(), urlOb, ttl, TimeUnit.SECONDS);
		return ResponseEntity.noContent().header("id", urlOb.getId()).build();
	}

	@SuppressWarnings("rawtypes")
	@GetMapping(value = "/{id}")
	public ResponseEntity getUrl(@PathVariable final String id) {
		final Url urlOb = redisTemplate.opsForValue().get(id);
		if (Objects.isNull(urlOb)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("No such key exists."));
		} else {
			log.info("URL retrieved = {}", urlOb.getUrl());
		}

		return ResponseEntity.ok(urlOb);
	}
}
