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

import com.auxime.contract.constants.DurationUnit;
import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.CommentCommercialPublic;
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.dto.commercial.CreateCommercialAmendment;
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
class CommercialContractControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountFeign proxy;

	private static final String PATH = "/commercialContract";
	private static final String IDTEST = "f4772f45-6acb-45d7-9220-7cd6c4a8ac04";
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
		assertEquals("commercial_contract", dataRecord.get("contractTypology"));
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
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.currentPage", is(1)))
				.andExpect(jsonPath("$.totalItems", is(1)))
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
				.andExpect(jsonPath("$.contractTypology", is("commercial_contract")))
				.andExpect(jsonPath("$.contractState", is("CANCELED")));
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
				.andExpect(jsonPath("$.globalAmount", is(60000.0)))
				.andExpect(jsonPath("$.monthlyAmount", is(10000.0)))
				.andExpect(jsonPath("$.missionDuration", is(6)))
				.andExpect(jsonPath("$.durationUnit", is("MONTHS"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		UUID id = UUID.fromString(context.read("$.contractId"));
		CommercialUpdate contractUpdate = updateContractModel();
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
				.andExpect(jsonPath("$.globalAmount", is(60000.0)))
				.andExpect(jsonPath("$.monthlyAmount", is(10000.0)))
				.andExpect(jsonPath("$.missionDuration", is(6)))
				.andExpect(jsonPath("$.durationUnit", is("MONTHS"))).andReturn();
		mockMvc.perform(get(PATH + "/details").param("contractId", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractTypology", is("commercial_contract")))
				.andExpect(jsonPath("$.contractState", is("NOT_STARTED")))
				.andExpect(jsonPath("$.contractTitle", is("It is a title updated")));
		mockMvc.perform(delete(PATH + "/delete/{contractId}", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(get(PATH + "/details").param("contractId", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.status", is(false)));
		mockMvc.perform(delete(PATH + "/delete/{contractId}", id.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		CreateCommercialAmendment amendment = createContractAmendmentModel();
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
				.andExpect(jsonPath("$.globalAmount", is(60000.0)))
				.andExpect(jsonPath("$.monthlyAmount", is(10000.0)))
				.andExpect(jsonPath("$.missionDuration", is(6)))
				.andExpect(jsonPath("$.durationUnit", is("MONTHS"))).andReturn();
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
		CreateCommercialAmendment amendment = createContractAmendmentModel();
		amendment.setContractAmendment(UUID.randomUUID());
		mockMvc.perform(post(PATH + "/createAmendment").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(amendment))).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		CommercialUpdate contractUpdate = updateContractModel();
		contractUpdate.setContractId(UUID.randomUUID());
		mockMvc.perform(put(PATH + "/update").contentType(MediaType.APPLICATION_JSON).content(asJsonString(contractUpdate)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
	}
	
	@Order(8)
	@Test
	@DisplayName("When going through validation process, execute all steps successfully")
	void givenValidationProcess_whenExecutingTasks_thenReturnCorrectAnswers() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		MvcResult mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		UUID id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		mockMvc.perform(put(PATH + "/validateContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		mockMvc.perform(put(PATH + "/refuseContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("PENDING_VALIDATION")));
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		CommentCommercialPublic comment = new CommentCommercialPublic();
		comment.setComment("Comment to modify");
		comment.setFirstName("FirstName");
		comment.setLastName("LastName");
		mockMvc.perform(put(PATH + "/askModification").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(comment)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("MODIFICATION_REQUIRED")))
				.andExpect(jsonPath("$.comments[0].comment", is("Comment to modify")))
				.andExpect(jsonPath("$.comments[0].firstName", is("FirstName")))
				.andExpect(jsonPath("$.comments[0].lastName", is("LastName")));
		mockMvc.perform(put(PATH + "/askModification").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(comment)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		comment = new CommentCommercialPublic();
		comment.setComment("Ok done");
		comment.setFirstName("Andree");
		comment.setLastName("Sylvie");
		mockMvc.perform(put(PATH + "/addComment").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(comment)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("MODIFICATION_REQUIRED")))
				.andExpect(jsonPath("$.comments").isArray())
				.andExpect(jsonPath("$.comments", hasSize(2)));
		mockMvc.perform(put(PATH + "/validateContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("VALIDATED")));
	}
	
	@Order(9)
	@Test
	@DisplayName("When validating a contract without modificatuion, then correct saving")
	void givenValidationWithNoModification_whenExecutingTasks_thenReturnCorrectAnswers() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		MvcResult mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		UUID id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("PENDING_VALIDATION")));
		mockMvc.perform(put(PATH + "/validateContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("VALIDATED")));
	}
	
	@Order(10)
	@Test
	@DisplayName("When refusing a contract without modificatuion, then correct saving")
	void givenRefusingWithNoModification_whenExecutingTasks_thenReturnCorrectAnswers() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		MvcResult mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		UUID id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("PENDING_VALIDATION")));
		mockMvc.perform(put(PATH + "/refuseContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("REFUSED")));
	}
	
	@Order(11)
	@Test
	@DisplayName("When refusing a contract with modificatuion, then correct saving")
	void givenRefusingWithModification_whenExecutingTasks_thenReturnCorrectAnswers() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		MvcResult mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		UUID id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("PENDING_VALIDATION")));
		CommentCommercialPublic comment = new CommentCommercialPublic();
		comment.setComment("Comment to modify");
		comment.setFirstName("FirstName");
		comment.setLastName("LastName");
		mockMvc.perform(put(PATH + "/askModification").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(comment)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("MODIFICATION_REQUIRED")));
		mockMvc.perform(put(PATH + "/refuseContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("REFUSED")));
	}
	
	@Order(12)
	@Test
	@DisplayName("When refusing a contract with modificatuion, then correct saving")
	void givenUnexistingContract_whenExecutingTasks_thenReturnErros() throws Exception {
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(createProfileInfoModel());
		UUID id = UUID.randomUUID();
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		CommentCommercialPublic comment = new CommentCommercialPublic();
		comment.setComment("Comment to modify");
		comment.setFirstName("FirstName");
		comment.setLastName("LastName");
		mockMvc.perform(put(PATH + "/askModification").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(comment)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		mockMvc.perform(put(PATH + "/refuseContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		mockMvc.perform(put(PATH + "/validateContract").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
		mockMvc.perform(put(PATH + "/addComment").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(comment)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND)));
	}
	
	@Order(13)
	@Test
	@DisplayName("When Asking validation, answer depending of the info returned in ProfileInfo")
	void givenAskingValidation_whenAnswering_thenReturnAnswerDependingOnProfile() throws Exception {
		Optional<ProfileInfo> profileInfo = createProfileInfoModel();
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(profileInfo);
		when(proxy.getAccountsyExist(any(UUID.class))).thenReturn(true);
		MvcResult mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		UUID id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		profileInfo.get().setManagerId(null);
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.contractStatus", is("PENDING_VALIDATION")));
		mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		response = mvcResult.getResponse().getContentAsString();
		id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		profileInfo.get().setManagerId(null);
		profileInfo.get().setBusinessManagerId(null);
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_INTERNAL_ERROR)));
		mvcResult = mockMvc
				.perform(post(PATH + "/create").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(createContractModel())))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.contractStatus", is("DRAFT"))).andReturn();
		response = mvcResult.getResponse().getContentAsString();
		id = UUID.fromString(JsonPath.parse(response).read("$.contractId"));
		when(proxy.getProfilesFromAccountId(any(UUID.class))).thenReturn(Optional.empty());
		mockMvc.perform(put(PATH + "/pendingValidation").param("contractId", id.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NO_VALIDATOR)));
	}

	private CommercialCreate createContractModel() {
		CommercialCreate contractCreate = new CommercialCreate();
		contractCreate.setAccountId(UUID.fromString("f99337eb-ff45-487a-a20d-f186ba71e99c"));
		contractCreate.setContractDate(LocalDate.now());
		contractCreate.setContractTitle("It is a title");
		contractCreate.setStartingDate(LocalDate.now().plusDays(7));
		contractCreate.setStructureContract(PortageCompanies.COELIS);
		contractCreate.setEndDate(LocalDate.now().plusMonths(6));
		contractCreate.setClientId(UUID.randomUUID());
		contractCreate.setGlobalAmount(60000.0);
		contractCreate.setMonthlyAmount(10000.0);
		contractCreate.setMissionDuration(6);
		contractCreate.setDurationUnit(DurationUnit.MONTHS);
		return contractCreate;
	}

	private CreateCommercialAmendment createContractAmendmentModel() {
		CreateCommercialAmendment contractCreate = new CreateCommercialAmendment();
		contractCreate.setAccountId(UUID.fromString("f99337eb-ff45-487a-a20d-f186ba71e99c"));
		contractCreate.setContractDate(LocalDate.now());
		contractCreate.setContractTitle("It is a title");
		contractCreate.setStartingDate(LocalDate.now().plusDays(7));
		contractCreate.setStructureContract(PortageCompanies.COELIS);
		contractCreate.setEndDate(LocalDate.now().plusMonths(6));
		contractCreate.setClientId(UUID.randomUUID());
		contractCreate.setGlobalAmount(60000.0);
		contractCreate.setMonthlyAmount(10000.0);
		contractCreate.setMissionDuration(6);
		contractCreate.setDurationUnit(DurationUnit.MONTHS);
		return contractCreate;
	}

	private CommercialUpdate updateContractModel() {
		CommercialUpdate contractUpdate = new CommercialUpdate();
		contractUpdate.setContractDate(LocalDate.now().plusWeeks(6));
		contractUpdate.setContractTitle("It is a title updated");
		contractUpdate.setStartingDate(LocalDate.now().plusDays(8));
		contractUpdate.setStructureContract(PortageCompanies.COELIS);
		contractUpdate.setEndDate(LocalDate.now().plusMonths(6));
		contractUpdate.setClientId(UUID.randomUUID());
		contractUpdate.setGlobalAmount(60000.0);
		contractUpdate.setMonthlyAmount(10000.0);
		contractUpdate.setMissionDuration(6);
		contractUpdate.setDurationUnit(DurationUnit.MONTHS);
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
