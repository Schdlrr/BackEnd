package com.schdlr.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schdlr.controller.BusinessOwnerController;
import com.schdlr.model.BusinessOwner;
import com.schdlr.service.BusinessOwnerService;
import com.schdlr.service.JWTService;
import com.schdlr.util.TokenAndCookiesUtil;
import com.schdlr.util.TokenExtractionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testable
@WebMvcTest(controllers = BusinessOwnerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BusinessOwnerControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BusinessOwnerService businessOwnerService;

	@MockBean
	private TokenExtractionUtil tokenExtractionUtil;

	@MockBean
	private TokenAndCookiesUtil tokenAndCookiesUtil;

	@MockBean
	private JWTService jwtService;

	@Autowired
	private ObjectMapper objectMapper;

	private BusinessOwner testOwner;

	@BeforeEach
	public void setUp(){
		testOwner = BusinessOwner.builder().userName("testOwner").email("testowner@gmail.com").password("password")
				.number("045210022").businessName("businessName").businessAddress("businessAddress").build();
	}

	@Test
	public void BusinessOwnerController_testTest_ReturnsString() throws Exception {

		mockMvc.perform(get("/api/business/test")
						.param("param", "example"))
				.andExpect(status().isOk())
				.andExpect(content().string("Welcome to the best website ever"));

	}

	@Test
	public void BusinessOwnerController_testSignUp_ReturnsUsernameAndHttpStatusCreated() throws Exception {
		given(businessOwnerService.signUp(testOwner))
				.willReturn(new ResponseEntity<>(testOwner.getUserName(), HttpStatus.CREATED));

		mockMvc.perform(post("/api/business/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testOwner)))
				.andExpect(status().isCreated())
				.andExpect(content().string(testOwner.getUserName()));

	}

}
