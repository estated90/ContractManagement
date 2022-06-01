package com.auxime.contract.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
				.andExpect(jsonPath("$.currentPage", is(1))).andExpect(jsonPath("$.totalItems", is(6)))
				.andExpect(jsonPath("$.totalPages", is(1))).andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(6))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		Configuration pathConfiguration = Configuration.builder().options(Option.AS_PATH_LIST).build();
		List<String> pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == 'dce6d0df-8d4f-4612-890a-79503dd67f8c')]");
		Map<String, String> dataRecord = context.read(pathList.get(0));
		assertEquals("cape", dataRecord.get("contractTypology"));
		assertEquals("f99337eb-ff45-487a-a20d-f186ba71e99c", dataRecord.get("accountId"));
		assertEquals("NOT_STARTED", dataRecord.get("contractState"));
		assertEquals("CAPE Monsieur du Test", dataRecord.get("contractTitle"));
		assertEquals("CONTRACT", dataRecord.get("contractType"));
		assertEquals(LocalDate.now().plusDays(30).toString(), dataRecord.get("startingDate"));
		assertEquals(true, dataRecord.get("fse"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == 'f4772f45-6acb-45d7-9220-7cd6c4a8ac04')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals("commercial_contract", dataRecord.get("contractTypology"));
		assertEquals("5b1a9d99-d0e7-49ca-a99e-82c867656fdc", dataRecord.get("clientId"));
		assertEquals("VALIDATED", dataRecord.get("contractStatus"));
		assertEquals("MONTHS", dataRecord.get("durationUnit"));
		assertEquals(12000.0, dataRecord.get("globalAmount"));
		assertEquals(12, dataRecord.get("missionDuration"));
		assertEquals(1000.0, dataRecord.get("monthlyAmount"));
		assertEquals("d2d12d0a-75ce-4051-9e22-95a00ede8138", dataRecord.get("validatorId"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == 'de72bff9-23aa-428d-91e9-c695c823ec7f')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals("permanent_contract", dataRecord.get("contractTypology"));
		assertEquals(LocalDate.now().plusDays(30).toString(), dataRecord.get("ruptureDate"));
		assertEquals(20.0, dataRecord.get("hourlyRate"));
		assertEquals(151.67, dataRecord.get("workTime"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == '46492afb-7f38-454a-90d8-cf284610e158')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals("temporary_contract", dataRecord.get("contractTypology"));
		assertEquals(LocalDate.now().plusDays(30).toString(), dataRecord.get("ruptureDate"));
		assertEquals(20.0, dataRecord.get("hourlyRate"));
		assertEquals(151.67, dataRecord.get("workTime"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == '3b2d1af5-faf5-4a0f-8e3d-4dcd70ab4ae1')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals("portage_convention", dataRecord.get("contractTypology"));
		assertEquals(10, dataRecord.get("commission"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == '696271ec-19ca-416b-acb2-cbce1e1bfefc')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals("cape", dataRecord.get("contractTypology"));
		assertEquals("dce6d0df-8d4f-4612-890a-79503dd67f8c", dataRecord.get("contractAmendment"));
	}

	@Test
	@DisplayName("When asked to contracts using id then Return the contracts")
	void givenContract_whenCalledById_thenReturnContract() throws Exception {
		String pathVariable = "dce6d0df-8d4f-4612-890a-79503dd67f8c";
		mockMvc.perform(get("/contracts/{contractId}", pathVariable)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("cape")));
		pathVariable = "f4772f45-6acb-45d7-9220-7cd6c4a8ac04";
		mockMvc.perform(get("/contracts/{contractId}", pathVariable)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("commercial_contract")));
		pathVariable = "de72bff9-23aa-428d-91e9-c695c823ec7f";
		mockMvc.perform(get("/contracts/{contractId}", pathVariable)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("permanent_contract")));
		pathVariable = "46492afb-7f38-454a-90d8-cf284610e158";
		mockMvc.perform(get("/contracts/{contractId}", pathVariable)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("temporary_contract")));
		pathVariable = "3b2d1af5-faf5-4a0f-8e3d-4dcd70ab4ae1";
		mockMvc.perform(get("/contracts/{contractId}", pathVariable)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("portage_convention")));
		pathVariable = UUID.randomUUID().toString();
		mockMvc.perform(get("/contracts/{contractId}", pathVariable)).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(result -> assertEquals("null", result.getResponse().getContentAsString()));
	}
	
	@Test
	@DisplayName("When using filter then Return the list of contracts and page details")
	void givenFilteredRequest_whenCalled_thenReturnFilteredContracts() throws Exception {
		mockMvc.perform(get("/contracts").param("page", "2")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(2)))
				.andExpect(jsonPath("$.totalItems", is(6)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(0)));
		mockMvc.perform(get("/contracts").param("size", "1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(6)))
				.andExpect(jsonPath("$.totalPages", is(6)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(1)));
		mockMvc.perform(get("/contracts").param("filter", "CDI")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(1)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(1)));
		mockMvc.perform(get("/contracts").param("filter", "")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(6)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(6)));
		mockMvc.perform(get("/contracts").param("contractState", "ACTIVE")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(2)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(2)));
		mockMvc.perform(get("/contracts").param("structureContract", "COELIS")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(3)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(3)));
		mockMvc.perform(get("/contracts").param("startDate", LocalDate.now().minusMonths(4).toString())
				.param("endDate", LocalDate.now().minusDays(45).toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(2)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(2)));
		mockMvc.perform(get("/contracts").param("startDate", LocalDate.now().toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(3)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(3)));
		mockMvc.perform(get("/contracts").param("endDate", LocalDate.now().plusDays(31).toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(4)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(4)));
	}

}
