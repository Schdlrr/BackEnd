package com.schdlr.Service;

import com.schdlr.service.GuestUserService;
import com.schdlr.service.JWTService;
import com.schdlr.util.TokenAndCookiesUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@Testable
@ExtendWith(MockitoExtension.class)
public class GuestUserServiceTest {

	@Mock
	private JWTService jwtService;

	@Mock
	private TokenAndCookiesUtil tokenAndCookiesUtil;

	@InjectMocks
	private GuestUserService guestUserService;

	@Mock
	private HttpServletResponse response;

	@Test
	public void testSignIn_Successful() throws Exception {

		String expectedAccessToken = "mockAccessToken";
		String expectedRefreshToken = "mockRefreshToken";

		given(jwtService.generateRefreshToken(anyString(), eq("GuestUser"))).willReturn(expectedRefreshToken);
		given(jwtService.generateAccessToken(anyString(), eq("GuestUser"))).willReturn(expectedAccessToken);


		ResponseEntity<String> result = guestUserService.signIn(response);


		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Guest user signed in");


		verify(jwtService).generateRefreshToken(anyString(), eq("GuestUser"));
		verify(jwtService).generateAccessToken(anyString(), eq("GuestUser"));
		verify(tokenAndCookiesUtil).addCookie(response, "accessToken", expectedAccessToken, 30 * 60, true, false, "Strict");
		verify(tokenAndCookiesUtil).addCookie(response, "refreshToken", expectedRefreshToken, 30 * 24 * 60 * 60, true, false, "Strict");
	}

	@Test
	public void testSignIn_NoSuchAlgorithmException() throws Exception {

		given(jwtService.generateRefreshToken(anyString(), eq("GuestUser"))).willThrow(NoSuchAlgorithmException.class);


		ResponseEntity<String> result = guestUserService.signIn(response);


		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(result.getBody()).isEqualTo("Algorithm for token does not exist");


		verify(jwtService).generateRefreshToken(anyString(), eq("GuestUser"));
		verifyNoInteractions(tokenAndCookiesUtil);
	}

	@Test
	public void testSignIn_InvalidKeySpecException() throws Exception {

		given(jwtService.generateRefreshToken(anyString(), eq("GuestUser"))).willThrow(InvalidKeySpecException.class);


		ResponseEntity<String> result = guestUserService.signIn(response);


		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(result.getBody()).isEqualTo("The key sppec used is invalid");

		verify(jwtService).generateRefreshToken(anyString(), eq("GuestUser"));
		verifyNoInteractions(tokenAndCookiesUtil); // Cookies should not be added
	}
}
