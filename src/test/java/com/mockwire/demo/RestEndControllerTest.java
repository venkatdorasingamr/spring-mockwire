package com.mockwire.demo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(WireMockExtension.class)
@ContextConfiguration(initializers = { WireMockInitializer.class })
class RestEndControllerTest {

	@Autowired
	private WireMockServer wireMockServer;

	@Autowired
	private WebTestClient webTestClient;

	@LocalServerPort
	private Integer port;

	@AfterEach
	public void afterEach() {
		this.wireMockServer.resetAll();
	}

	@Test
	void testGetAllTodosShouldReturnDataFromClient() {
		this.wireMockServer.stubFor(WireMock.get("/todos").willReturn(aResponse()
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody("[{\"userId\": 1,\"id\": 1,\"title\": \"delectus aut autem\", \"completed\": false},"
						+ "{\"userId\": 1,\"id\": 2,\"title\": \"quis ut nam facilis et officia qui\", \"completed\": false}]")));

		this.webTestClient
			.get()
			.uri("/api/todos")
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBody()
			.jsonPath("$[0].title")
			.isEqualTo("delectus aut autem")
			.jsonPath("$[0].completed")
			.isEqualTo(true);
	}

	@Test
	void testGetAllTodosShouldgetSuccessMessageTest() {
		this.wireMockServer
				.stubFor(WireMock.get("/todos").willReturn(aResponse().withStatus(403).withFixedDelay(2000)));

		this.webTestClient.get().uri("http://localhost:" + port + "/api/todos").exchange().expectStatus()
				.isEqualTo(HttpStatus.OK_200);
	}
}