package com.project.urlshortner.controllertest;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UrlShortningTests extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void generateUrl() throws Exception {
		String uri = "/rest/url";

		String inputJson = "{\r\n" + "	\"url\" : \"https://www.facebook.com/\"\r\n" + "}";
		String resultString = "shortned Url = https://shorturl/f8454a38";

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String expected = mvcResult.getResponse().getContentAsString();

		assertEquals(expected, resultString);
	}

	@Test
	public void testGeneratedUrl() throws Exception {
		String uri = "/rest/url/f8454a38";

		mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", is("f8454a38"))).andExpect(MockMvcResultMatchers
						.jsonPath("$.url", is("{\r\n\t\"url\" : \"https://www.facebook.com/\"\r\n}")));

	}

}
