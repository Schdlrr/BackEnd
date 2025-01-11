package com.schdlr.Controller;

import com.schdlr.controller.GuestUserController;
import com.schdlr.service.GuestUserService;
import com.schdlr.util.TokenAndCookiesUtil;
import com.schdlr.util.TokenExtractionUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testable
@WebMvcTest(controllers = GuestUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GuestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GuestUserService guestUserService;

	@MockBean
	private TokenExtractionUtil tokenExtractionUtil;

	@MockBean
	private TokenAndCookiesUtil tokenAndCookiesUtil;

	@Test
	public void GuestController_testSignIn_ReturnsOkAndSignedIn() throws Exception {
		given(guestUserService.signIn(any(HttpServletResponse.class)))
				.willReturn(new ResponseEntity<>("Guest user signed in", HttpStatus.OK));

		mockMvc.perform(post("/api/guest/signin"))
				.andExpect(status().isOk())
				.andExpect(content().string("Guest user signed in"));

		verify(guestUserService, times(1)).signIn(any(HttpServletResponse.class));
	}

	@Test
	public void testSignIn_ReturnsInternalServerError() throws Exception {

		HttpServletResponse response = mock(HttpServletResponse.class);
		given(guestUserService.signIn(any(HttpServletResponse.class)))
				.willThrow(new RuntimeException("Unexpected error"));


		mockMvc.perform(post("/api/guest/signin"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("An internal server error occurred"));

		// Verify that the service method was called once
		verify(guestUserService, times(1)).signIn(any(HttpServletResponse.class));
	}

}
