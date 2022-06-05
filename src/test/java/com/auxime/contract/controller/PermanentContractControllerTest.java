package com.auxime.contract.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
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
import com.auxime.contract.dto.permanent.CreatePermanentAmendment;
import com.auxime.contract.dto.permanent.PermanentCreate;
import com.auxime.contract.dto.permanent.PermanentUpdate;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.enums.PortageCompanies;
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
class PermanentContractControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountFeign proxy;

	private static final String PATH = "/permanentContract";
	private static final String IDTEST = "de72bff9-23aa-428d-91e9-c695c823ec7f";
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
	@DisplayName("When asked to return all commercial Contract then Return the list of commercial Contract and page details")
	void givenApiAllContract_whenCalled_thenReturnAllContracts() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH + "/list"))
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
		assertEquals("permanent_contract", dataRecord.get("contractTypology"));
		assertNull(dataRecord.get("contractAmendment"));
	}

	@Order(3)
	@Test
	@DisplayName("When asked to return all accounts from accounts then return the capes linked to it")
	void givenFilterOnAccount_whenGettingAllContract_thenReturnAllFilteredContracts() throws Exception {
		mockMvc.perform(get(PATH + "/listAccount").param("accountId", IDACCOUNT))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(2)))
				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(2)));
		mockMvc.perform(get(PATH + "/listAccount").param("accountId", UUID.randomUUID().toString()))
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
		mockMvc.perform(get(PATH + "/listAmendment").param("contractId", IDTEST))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.currentPage", is(1)))
 				.andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.contracts").isArray())
				.andExpect(jsonPath("$.contracts", hasSize(1)));
		mockMvc.perform(get(PATH + "/listAmendment").param("contractId", UUID.randomUUID().toString()))
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
		mockMvc.perform(get(PATH + "/details").param("contractId", IDTEST))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("permanent_contract")))
				.andExpect(jsonPath("$.contractState", is("ACTIVE")));
		mockMvc.perform(get(PATH + "/details").param("contractId", UUID.randomUUID().toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(result -> assertEquals("null", result.getResponse().getContentAsString()));
	}

	@MockBean
	private PdfGenerator pdfGenerator;

	@Order(6)
	@Test
	@DisplayName("When creating a contract then return newly created object")
	void givenCreatingCape_whenSendingInfos_thenReturnContracts() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		Mockito.doNothing().when(pdfGenerator).replaceTextModel(anyMap(), anyString(), anyString());
		MvcResult mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.accountId", is("f99337eb-ff45-487a-a20d-f186ba71e99c")))
				.andExpect(jsonPath("$.contractDate", is(LocalDate.now().toString())))
				.andExpect(jsonPath("$.contractTitle", is("It is a title")))
				.andExpect(jsonPath("$.startingDate", is(LocalDate.now().plusDays(7).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS")))
				.andExpect(jsonPath("$.endDate", is(LocalDate.now().plusMonths(6).toString())))
				.andExpect(jsonPath("$.ruptureDate", is(LocalDate.now().plusMonths(5).toString())))
				.andExpect(jsonPath("$.hourlyRate", is(30.0)))
				.andExpect(jsonPath("$.workTime", is(151.67))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		UUID id = UUID.fromString(context.read("$.contractId"));
		PermanentUpdate contractUpdate = updateContractModel();
		contractUpdate.setContractId(id);
		mockMvc.perform(put(PATH + "/update").contentType(MediaType.APPLICATION_JSON).content(asJsonString(contractUpdate)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.accountId", is("f99337eb-ff45-487a-a20d-f186ba71e99c")))
				.andExpect(jsonPath("$.contractDate", is(LocalDate.now().plusWeeks(6).toString())))
				.andExpect(jsonPath("$.contractTitle", is("It is a title updated")))
				.andExpect(jsonPath("$.startingDate", is(LocalDate.now().plusDays(8).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS")))
				.andExpect(jsonPath("$.endDate", is(LocalDate.now().plusMonths(6).toString())))
				.andExpect(jsonPath("$.ruptureDate", is(LocalDate.now().plusMonths(5).toString())))
				.andExpect(jsonPath("$.hourlyRate", is(30.0)))
				.andExpect(jsonPath("$.workTime", is(151.67))).andReturn();
		mockMvc.perform(get(PATH + "/details").param("contractId", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("permanent_contract")))
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.contractTitle", is("It is a title updated")));
		mockMvc.perform(delete(PATH + "/delete/{contractId}", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(get(PATH + "/details").param("contractId", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.status", is(false)));
		mockMvc.perform(delete(PATH + "/delete/{contractId}", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		CreatePermanentAmendment amendment = createContractAmendmentModel();
		amendment.setContractAmendment(id);
		mvcResult = mockMvc
				.perform(post(PATH + "/createAmendment").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(amendment)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.accountId", is("f99337eb-ff45-487a-a20d-f186ba71e99c")))
				.andExpect(jsonPath("$.contractDate", is(LocalDate.now().toString())))
				.andExpect(jsonPath("$.contractTitle", is("It is a title")))
				.andExpect(jsonPath("$.startingDate", is(LocalDate.now().plusDays(7).toString())))
				.andExpect(jsonPath("$.structureContract", is("COELIS")))
				.andExpect(jsonPath("$.endDate", is(LocalDate.now().plusMonths(6).toString())))
				.andExpect(jsonPath("$.ruptureDate", is(LocalDate.now().plusMonths(5).toString())))
				.andExpect(jsonPath("$.hourlyRate", is(30.0)))
				.andExpect(jsonPath("$.workTime", is(151.67))).andReturn();
		response = mvcResult.getResponse().getContentAsString();
		context = JsonPath.parse(response);
		id = UUID.randomUUID();
		mockMvc.perform(delete(PATH + "/delete/{contractId}", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
	}

	@Order(7)
	@Test
	@DisplayName("When creating a contract with unknown accout or profile info then return error")
	void givenCreatingCape_whenSendingUnexistingInfos_thenReturnErrors() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(false);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(Optional.empty());
		mockMvc.perform(
				post(PATH + "/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.ACCOUNT_NOT_FOUND)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		mockMvc.perform(
				post(PATH + "/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
		mockMvc.perform(
				post(PATH + "/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
		CreatePermanentAmendment amendment = createContractAmendmentModel();
		amendment.setContractAmendment(UUID.randomUUID());
		mockMvc.perform(post(PATH + "/createAmendment").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(amendment))).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND)));
		PermanentUpdate update = updateContractModel();
		update.setContractId(UUID.randomUUID());
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		mockMvc.perform(
				put(PATH + "/update").contentType(MediaType.APPLICATION_JSON).content(asJsonString(update)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND)))
				.andExpect(jsonPath("$.errors", hasItem("Bad request, unable to perform request")));
	}

	private PermanentCreate createContractModel() {
		PermanentCreate contractCreate = new PermanentCreate();
		contractCreate.setAccountId(UUID.fromString("f99337eb-ff45-487a-a20d-f186ba71e99c"));
		contractCreate.setContractDate(LocalDate.now());
		contractCreate.setContractTitle("It is a title");
		contractCreate.setStartingDate(LocalDate.now().plusDays(7));
		contractCreate.setStructureContract(PortageCompanies.COELIS);
		contractCreate.setEndDate(LocalDate.now().plusMonths(6));
		contractCreate.setRuptureDate(LocalDate.now().plusMonths(5));
		contractCreate.setFse(false);
		contractCreate.setHourlyRate(30.0);
		contractCreate.setWorkTime(151.67);
		return contractCreate;
	}

	private CreatePermanentAmendment createContractAmendmentModel() {
		CreatePermanentAmendment contractCreate = new CreatePermanentAmendment();
		contractCreate.setAccountId(UUID.fromString("f99337eb-ff45-487a-a20d-f186ba71e99c"));
		contractCreate.setContractDate(LocalDate.now());
		contractCreate.setContractTitle("It is a title");
		contractCreate.setStartingDate(LocalDate.now().plusDays(7));
		contractCreate.setStructureContract(PortageCompanies.COELIS);
		contractCreate.setEndDate(LocalDate.now().plusMonths(6));
		contractCreate.setRuptureDate(LocalDate.now().plusMonths(5));
		contractCreate.setFse(false);
		contractCreate.setHourlyRate(30.0);
		contractCreate.setWorkTime(151.67);
		return contractCreate;
	}

	private PermanentUpdate updateContractModel() {
		PermanentUpdate contractUpdate = new PermanentUpdate();
		contractUpdate.setContractDate(LocalDate.now().plusWeeks(6));
		contractUpdate.setContractTitle("It is a title updated");
		contractUpdate.setStartingDate(LocalDate.now().plusDays(8));
		contractUpdate.setStructureContract(PortageCompanies.COELIS);
		contractUpdate.setEndDate(LocalDate.now().plusMonths(6));
		contractUpdate.setRuptureDate(LocalDate.now().plusMonths(5));
		contractUpdate.setFse(false);
		contractUpdate.setHourlyRate(30.0);
		contractUpdate.setWorkTime(151.67);
		return contractUpdate;
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
