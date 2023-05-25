package com.mockwire.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	private final static String base1Url = "https://jsonplaceholder.typicode.com";

	@Bean
	public WebClient todoWebClient(@Value("u_base_url") String baseUrl, WebClient.Builder webClientBuilder) {

		return webClientBuilder
				.baseUrl(base1Url)
				.defaultHeader(HttpHeaders.ACCEPT, org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}