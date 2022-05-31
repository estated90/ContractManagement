package com.auxime.contract.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class ContractControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("When asked to return all contracts then Return the list of contracts and page details")
	void givenApiAllContract_whenCalled_thenReturnAllContracts() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/contracts")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(6)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(6)))
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		Configuration pathConfiguration = 
				  Configuration.builder().options(Option.AS_PATH_LIST).build();
		List<String> pathList = JsonPath.using(pathConfiguration)
	            .parse(response).read("$.contracts[?(@['contractId'] == 'dce6d0df-8d4f-4612-890a-79503dd67f8c')]");
		Map<String, String> dataRecord = context.read(pathList.get(0));
		assertEquals("cape", dataRecord.get("contractTypology"));
		assertEquals("f99337eb-ff45-487a-a20d-f186ba71e99c", dataRecord.get("accountId"));
		assertEquals("NOT_STARTED", dataRecord.get("contractState"));
		assertEquals("CAPE Monsieur du Test", dataRecord.get("contractTitle"));
		assertEquals("CONTRACT", dataRecord.get("contractType"));
		assertEquals(LocalDate.now().toString(), dataRecord.get("startingDate"));
		assertEquals(true, dataRecord.get("fse"));
		}

}
