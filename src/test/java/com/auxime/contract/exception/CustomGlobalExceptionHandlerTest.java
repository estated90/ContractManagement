package com.auxime.contract.exception;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.auxime.contract.dto.cape.CapeCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class CustomGlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@DisplayName("Error CRUD operation with wrong method")
	void whenCrudOperationWrongMethod_thenReturnError() throws Exception {
		mockMvc.perform(put("/cape/listCape")).andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
				.andExpect(jsonPath("$.status", is("METHOD_NOT_ALLOWED")))
				.andExpect(jsonPath("$.message", is("Request method 'PUT' not supported")))
				.andExpect(jsonPath("$.errors").isArray()).andExpect(jsonPath("$.errors",
						hasItem("PUT method is not supported for this request. Supported methods are GET ")));
	}

	@Test
	@DisplayName("Error CRUD operation without parameter")
	void whenCrudOperationNoParameter_ThenReturnJsonError() throws Exception {
		System.out.println("-------------Error getting one profile without parameter------------------");
		mockMvc.perform(get("/cape/details")).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.message",
						is("Required request parameter 'capeId' for method parameter type UUID is not present")))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasItem("capeId parameter is missing")));
	}

	@DisplayName("Error CRUD operation when media type is incorrect")
	@Test
	void whenCrudOperationWrongMediaType_ThenReturnJsonError() throws Exception {
		CapeCreate capeCreate = new CapeCreate();
		mockMvc.perform(post("/cape/create").contentType(MediaType.TEXT_PLAIN).content(asJsonString(capeCreate)))
				.andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
				.andExpect(jsonPath("$.message", is("Content type 'text/plain' not supported")))
				.andExpect(jsonPath("$.errors", hasItem(
						"text/plain media type is not supported. Supported media types are application/json application/*+jso")));
	}

	@DisplayName("Error CRUD operation when type is incorrect")
	@Test
	void whenCrudOperationWrongType_ThenReturnJsonError() throws Exception {
		mockMvc.perform(get("/cape/details")
				.param("capeId", "i").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message", is(
						"Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: i")))
				.andExpect(jsonPath("$.errors", hasItem("capeId should be of type java.util.UUID")));
	}

	@DisplayName("Error CRUD operation when validation fail")
	@Test
	void whenCrudOperationValidationFail_ThenReturnJsonError() throws Exception {
		CapeCreate capeCreate = new CapeCreate();
		mockMvc.perform(
				post("/cape/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(capeCreate)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.errors", hasItem("contractDate: must not be null")))
				.andExpect(jsonPath("$.errors", hasItem("accountId: Portage Id cannot be null")))
				.andExpect(jsonPath("$.errors", hasItem("structureContract: must not be null")))
				.andExpect(jsonPath("$.errors", hasItem("contractTitle: must not be null")))
				.andExpect(jsonPath("$.errors", hasItem("startingDate: must not be null")))
				.andExpect(jsonPath("$.errors", hasItem("fse: must not be null")))
				.andExpect(jsonPath("$.errors", hasItem("rates: must not be null")));
	}
	
	@DisplayName("Error for type mismatch")
	@Test
	void whenCrudUUsingType_ThenReturnJsonError() throws Exception {
		mockMvc.perform(
				post("/cape/create").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.message", is("Required request body is missing: public org.springframework.http.ResponseEntity<com.auxime.contract.model.Cape> com.auxime.contract.controller.CapeController.createContract(com.auxime.contract.dto.cape.CapeCreate) throws com.auxime.contract.exception.PdfGeneratorException,com.auxime.contract.exception.CapeException")))
				.andExpect(jsonPath("$.errors", hasItem("An error occured while deserializing the data")));
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
