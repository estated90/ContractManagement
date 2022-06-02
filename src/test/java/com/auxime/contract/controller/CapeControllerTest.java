package com.auxime.contract.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.RateDto;
import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.dto.cape.CreateCapeAmendment;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.model.enums.TypeRate;
import com.auxime.contract.proxy.AccountFeign;
import com.auxime.contract.utils.PdfGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(OrderAnnotation.class)
class CapeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountFeign proxyCape;
	@MockBean
	private PdfGenerator pdfGenerator;
	
	private static final String PATH = "/cape";
	private static final String IDTEST = "dce6d0df-8d4f-4612-890a-79503dd67f8c";
	private static final String IDAMENDMENTTEST = "696271ec-19ca-416b-acb2-cbce1e1bfefc";
	private static final String IDACCOUNT = "f99337eb-ff45-487a-a20d-f186ba71e99c";
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@AfterEach
	void tearDown() throws Exception {
	}

	@Order(1)
	@Test
	@DisplayName("When asked to return all Cape then Return the list of cape and page details")
	void givenApiAllCape_whenCalled_thenReturnAllCape() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH + "/listCape"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(2)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(2))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		Configuration pathConfiguration = Configuration.builder().options(Option.AS_PATH_LIST).build();
		List<String> pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == '" + IDTEST + "')]");
		Map<String, String> dataRecord = context.read(pathList.get(0));
		assertEquals("cape", dataRecord.get("contractTypology"));
		assertNull(dataRecord.get("contractAmendment"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.contracts[?(@['contractId'] == '" + IDAMENDMENTTEST + "')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals("cape", dataRecord.get("contractTypology"));
		assertEquals(IDTEST, dataRecord.get("contractAmendment"));
	}
	
	@Order(2)
	@Test
	@DisplayName("When asked to return all Cape with filter on Rates then Return the list of cape filtered and page details")
	void givenApiFilterRate_whenGettingAllContract_thenReturnAllFilteredContracts() throws Exception {
		mockMvc.perform(get(PATH + "/listCape").param("rate", "12"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(1)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(1)));
	}
	
	@Order(3)
	@Test
	@DisplayName("When asked to return all accounts from accounts then return the capes linked to it")
	void givenFilterOnAccount_whenGettingAllContract_thenReturnAllFilteredContracts() throws Exception {
		mockMvc.perform(get(PATH + "/listCapeAccount").param("accountId", IDACCOUNT))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(2)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(2)));
		mockMvc.perform(get(PATH + "/listCapeAccount").param("accountId", UUID.randomUUID().toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(0)))
				.andExpect(jsonPath("$.totalPages", is(0)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(0)));
	}
	
	@Order(4)
	@Test
	@DisplayName("When asked to return all contract amendment from accounts then return the amendment linked to it")
	void givenAskingAmendment_whenGettingAmendments_thenReturnAllFilteredContracts() throws Exception {
		mockMvc.perform(get(PATH + "/listCapeAmendment").param("contractId", IDTEST))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(1)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(1)));
		mockMvc.perform(get(PATH + "/listCapeAmendment").param("contractId", UUID.randomUUID().toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(0)))
				.andExpect(jsonPath("$.totalPages", is(0)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(0)));
	}
	
	@Order(5)
	@Test
	@DisplayName("When asked to return detail contract from then return the contract")
	void givenContract_whenGettingDetails_thenReturnContracts() throws Exception {
		mockMvc.perform(get(PATH + "/details").param("capeId", IDTEST))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("cape")))
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")));
		mockMvc.perform(get(PATH + "/details").param("capeId", UUID.randomUUID().toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(result -> assertEquals("null", result.getResponse().getContentAsString()));
	}
	
	@Order(6)
	@Test
	@DisplayName("When creating a contract then return newly created object")
	void givenCreatingCape_whenSendingInfos_thenReturnContracts() throws Exception {
		when(proxyCape.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxyCape.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		Mockito.doNothing().when(pdfGenerator).replaceTextModel(anyMap(), anyString(), anyString());
		MvcResult mvcResult = mockMvc.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(createCapeModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.accountId", is("f99337eb-ff45-487a-a20d-f186ba71e99c")))
				.andExpect(jsonPath("$.contractDate", is(LocalDate.now().toString())))
				.andExpect(jsonPath("$.contractTitle", is("It is a title")))
				.andExpect(jsonPath("$.fse", is(true)))
				.andExpect(jsonPath("$.startingDate", is(LocalDate.now().plusDays(7).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS")))
				.andExpect(jsonPath("$.endDate", is(LocalDate.now().plusDays(7).plusYears(1).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		Configuration pathConfiguration = Configuration.builder().options(Option.AS_PATH_LIST).build();
		List<String> pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.rates[?(@['rate'] == '50')]");
		Map<String, Object> dataRecord = context.read(pathList.get(0));
		assertEquals(50, dataRecord.get("rate"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.rates[?(@['rate'] == '15')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals(15, dataRecord.get("rate"));
		UUID id = UUID.fromString(context.read("$.contractId"));
		CapeUpdate capeUpdate =  updateCapeModel();
		capeUpdate.setContractId(id);
		mockMvc.perform(put(PATH + "/update").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(capeUpdate)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.accountId", is("f99337eb-ff45-487a-a20d-f186ba71e99c")))
				.andExpect(jsonPath("$.contractDate", is(LocalDate.now().plusWeeks(6).toString())))
				.andExpect(jsonPath("$.contractTitle", is("It is a title updated")))
				.andExpect(jsonPath("$.fse", is(false)))
				.andExpect(jsonPath("$.startingDate", is(LocalDate.now().plusDays(8).toString())))
				.andExpect(jsonPath("$.structureContract", is("AUXIME")))
				.andExpect(jsonPath("$.endDate", is(LocalDate.now().plusDays(8).plusYears(1).toString()))).andReturn();
		mockMvc.perform(get(PATH + "/details").param("capeId", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("cape")))
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.contractTitle", is("It is a title updated")));
		mockMvc.perform(delete(PATH + "/delete/{capeId}",id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(get(PATH + "/details").param("capeId", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.status", is(false)));
		mockMvc.perform(delete(PATH + "/delete/{capeId}",id.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.CAPE_NOT_FOUND)));
		CreateCapeAmendment amendment = createCapeAmendmentModel();
		amendment.setContractAmendment(id);
		mvcResult = mockMvc.perform(post(PATH + "/createAmendment").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(amendment)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.accountId", is("f99337eb-ff45-487a-a20d-f186ba71e99c")))
				.andExpect(jsonPath("$.contractDate", is(LocalDate.now().toString())))
				.andExpect(jsonPath("$.contractTitle", is("It is a title")))
				.andExpect(jsonPath("$.fse", is(true)))
				.andExpect(jsonPath("$.startingDate", is(LocalDate.now().plusDays(7).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS")))
				.andExpect(jsonPath("$.endDate", is(LocalDate.now().plusDays(7).plusYears(1).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS"))).andReturn();
		response = mvcResult.getResponse().getContentAsString();
		context = JsonPath.parse(response);
		pathConfiguration = Configuration.builder().options(Option.AS_PATH_LIST).build();
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.rates[?(@['rate'] == '50')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals(50, dataRecord.get("rate"));
		pathList = JsonPath.using(pathConfiguration).parse(response)
				.read("$.rates[?(@['rate'] == '15')]");
		dataRecord = context.read(pathList.get(0));
		assertEquals(15, dataRecord.get("rate"));
		id = UUID.randomUUID();
		mockMvc.perform(delete(PATH + "/delete/{capeId}",id.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.CAPE_NOT_FOUND)));
	}

	@Order(7)
	@Test
	@DisplayName("When creating a contract with unknown accout or profile info then return error")
	void givenCreatingCape_whenSendingUnexistingInfos_thenReturnErrors() throws Exception {
		when(proxyCape.getAccountsyExist(any(UUID.class))).thenReturn(false);
		when(proxyCape.getProfilesFromAccountId(any(UUID.class))).thenReturn(Optional.empty());
		mockMvc.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(createCapeModel())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.ACCOUNT_NOT_FOUND)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
		when(proxyCape.getAccountsyExist(any(UUID.class))).thenReturn(true);
		mockMvc.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(createCapeModel())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
		mockMvc.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(createCapeModel())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
		CreateCapeAmendment amendment = createCapeAmendmentModel();
		amendment.setContractAmendment(UUID.randomUUID());
		mockMvc.perform(post(PATH + "/createAmendment").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(amendment)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.CAPE_NOT_FOUND)));
	}
	
	private CapeCreate createCapeModel() {
		CapeCreate createCape = new CapeCreate();
		createCape.setAccountId(UUID.fromString("f99337eb-ff45-487a-a20d-f186ba71e99c"));
		createCape.setContractDate(LocalDate.now());
		createCape.setContractTitle("It is a title");
		createCape.setFse(true);
		createCape.setStartingDate(LocalDate.now().plusDays(7));
		createCape.setStructureContract(PortageCompanies.COELIS);
		List<RateDto> rates = new ArrayList<>();
		RateDto rate = new RateDto();
		rate.setRate(15);
		rate.setTypeRate(TypeRate.ACTIVITY);
		rates.add(rate);
		rate = new RateDto();
		rate.setRate(50);
		rate.setTypeRate(TypeRate.QUALIOPI);
		rates.add(rate);
		createCape.setRates(rates);
		return createCape;
	}
	
	
	private CreateCapeAmendment createCapeAmendmentModel() {
		CreateCapeAmendment createCapeAmendment = new CreateCapeAmendment();
		createCapeAmendment.setAccountId(UUID.fromString("f99337eb-ff45-487a-a20d-f186ba71e99c"));
		createCapeAmendment.setContractDate(LocalDate.now());
		createCapeAmendment.setContractTitle("It is a title");
		createCapeAmendment.setFse(true);
		createCapeAmendment.setStartingDate(LocalDate.now().plusDays(7));
		createCapeAmendment.setStructureContract(PortageCompanies.COELIS);
		List<RateDto> rates = new ArrayList<>();
		RateDto rate = new RateDto();
		rate.setRate(15);
		rate.setTypeRate(TypeRate.ACTIVITY);
		rates.add(rate);
		rate = new RateDto();
		rate.setRate(50);
		rate.setTypeRate(TypeRate.QUALIOPI);
		rates.add(rate);
		createCapeAmendment.setRates(rates);
		return createCapeAmendment;
	}
	
	private CapeUpdate updateCapeModel() {
		CapeUpdate capeUpdate = new CapeUpdate();
		capeUpdate.setContractDate(LocalDate.now().plusWeeks(6));
		capeUpdate.setContractTitle("It is a title updated");
		capeUpdate.setFse(false);
		capeUpdate.setStartingDate(LocalDate.now().plusDays(8));
		capeUpdate.setStructureContract(PortageCompanies.AUXIME);
		return capeUpdate;
	}
	
	private Optional<ProfileInfo> createProfileInfoModel() {
		ProfileInfo profileInfo = new ProfileInfo();
		profileInfo.setActivity("Builder");
		profileInfo.setBirthCountry("France");
		profileInfo.setBirthdate(LocalDate.now());
		profileInfo.setBirthPlace("Paris");
		profileInfo.setBusinessManagerId(UUID.randomUUID());
		profileInfo.setCity("Lyon");
		profileInfo.setCountry("France");
		profileInfo.setFistName("Daniel");
		profileInfo.setLastName("Emmanuel");
		profileInfo.setManagerId(UUID.randomUUID());
		profileInfo.setNationality("Française");
		profileInfo.setNumber(1);
		profileInfo.setSocialSecurityNumber("908089090909");
		profileInfo.setStreet("atlantic street");
		profileInfo.setTitle("Monsieur");
		profileInfo.setZip("69001");
		return Optional.of(profileInfo);
	}
	
	private static String asJsonString(final Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
