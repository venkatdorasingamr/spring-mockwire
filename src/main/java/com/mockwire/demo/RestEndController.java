package com.mockwire.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
@RequestMapping("/api/todos")
public class RestEndController {

	private final WebClient todoWebClient;

	public RestEndController(WebClient todoWebClient) {
		this.todoWebClient = todoWebClient;
	}

	@GetMapping
	public ArrayNode getAllTodos() {
		return this.todoWebClient.get().uri("/todos").header("X-Auth", "duke").retrieve().bodyToMono(ArrayNode.class)
				.block();
	}
}