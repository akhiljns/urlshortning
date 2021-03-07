package com.project.urlshortner.model;

import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Url {

	private final String id;
	private final String url;
	private final LocalDateTime creationTs;

	public static Url createUsingMurmur(final String url) {
		final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
		return new Url(id, url, LocalDateTime.now());
	}

	public static Url createUsingSha(final String url) {
		final String id = Hashing.sha512().hashString(url, StandardCharsets.UTF_8).toString();
		return new Url(id, url, LocalDateTime.now());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("shortned Url = https://shorturl/");
		builder.append(id);
		return builder.toString();
	}

}
