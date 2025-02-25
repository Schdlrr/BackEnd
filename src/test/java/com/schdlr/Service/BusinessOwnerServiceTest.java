package com.schdlr.Service;

import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;
import com.schdlr.service.BusinessOwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@Testable
@ExtendWith(MockitoExtension.class)
public class BusinessOwnerServiceTest {

	@Mock
	private BusinessOwnerRepo mockBusinessOwnerRepo;

	@Mock
	private BCryptPasswordEncoder mockEncoder;

	@InjectMocks
	private BusinessOwnerService businessOwnerService;

	private BusinessOwner testOwner;

	@BeforeEach
	public void setUp(){
		testOwner = BusinessOwner.builder().userName("testOwner")
				.email("erdisyla6@gmail.com").password("password")
				.number("044221100").businessAddress("businessAddress")
				.businessName("businessName").build();
	}

	@Test
	public void BusinessOwnerService_testSignUp_ReturnsCorrectResponse(){
		given(mockEncoder.encode(testOwner.getPassword()))
				.willReturn("encodedPassword");
		given(mockBusinessOwnerRepo.save(testOwner))
				.willReturn(testOwner);

		ResponseEntity<String> response = businessOwnerService.signUp(testOwner);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo("testOwner");
		verify(mockEncoder,times(1)).encode("password");
		verify(mockBusinessOwnerRepo, times(1)).save(testOwner);

	}

	@Test
	public void BusinessOwnerService_testSignUp_EmailIsNotValid(){
		testOwner.setEmail("this is not a valid email");

		ResponseEntity<String> response = businessOwnerService.signUp(testOwner);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
		assertThat(response.getBody()).isEqualTo("Non valid email format entered.Please change email");
		verifyNoInteractions(mockEncoder);
		verifyNoInteractions(mockBusinessOwnerRepo);
	}

	@Test
	public void BusinessOwnerService_testSignUp_EmailIsAlreadyInUse(){
		given(mockBusinessOwnerRepo.findByEmail(testOwner.getEmail()))
				.willReturn(Optional.of(testOwner));

		ResponseEntity<String> response = businessOwnerService.signUp(testOwner);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(response.getBody())
				.isEqualTo("Email is already used by another user. Please try to sign up with another kind of contact info");
		verifyNoInteractions(mockEncoder);
	}

	@Test
	public void BusinessOwnerService_testSignIn_ReturnsCorrectResponse(){
		given(mockBusinessOwnerRepo.findByEmail("erdisyla6@gmail.com"))
				.willReturn(Optional.of(testOwner));
		given(mockEncoder.matches(testOwner.getPassword(),testOwner.getPassword()))
				.willReturn(true);


		ResponseEntity<String> response = businessOwnerService.signIn(testOwner.getUserName(),testOwner.getEmail(),testOwner.getPassword());

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("testOwner");
		verify(mockBusinessOwnerRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verify(mockEncoder,times(1))
				.matches(testOwner.getPassword(),testOwner.getPassword());

	}

	@Test
	public void BusinessOwnerService_testSignIn_ReturnsBadRequest(){
		given(mockBusinessOwnerRepo.findByEmail(testOwner.getEmail()))
				.willReturn(Optional.empty());

		ResponseEntity<String> response = businessOwnerService.signIn(testOwner.getUserName(),testOwner.getEmail(),testOwner.getPassword());

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isEqualTo("User not present in database");
		verify(mockBusinessOwnerRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verifyNoInteractions(mockEncoder);

	}

	@Test
	public void BusinessOwnerService_testSignIn_ReturnsUnauthorized(){
		given(mockBusinessOwnerRepo.findByEmail(testOwner.getEmail()))
				.willReturn(Optional.of(testOwner));
		given(mockEncoder.matches(testOwner.getPassword(),testOwner.getPassword()))
				.willReturn(false);

		ResponseEntity<String> response = businessOwnerService.signIn(testOwner.getUserName(),testOwner.getEmail(),testOwner.getPassword());

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).isEqualTo("The credentials that were used do not match to any user please try again");
		verify(mockBusinessOwnerRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verify(mockEncoder,times(1))
				.matches(testOwner.getPassword(),testOwner.getPassword());
	}

	@Test
	public void BusinessOwnerService_testIsValidEmail_ReturnsTrue(){
		boolean isValidEmail = businessOwnerService.isValidEmail(testOwner.getEmail());

		assertThat(isValidEmail).isTrue();
	}

	@Test
	public void BusinessOwnerService_testIsValidEmail_ReturnsFalse(){
		boolean isValidEmail = businessOwnerService.isValidEmail("invalidemail.com");

		assertThat(isValidEmail).isFalse();
	}

	@Test
	public void BusinessOwnerService_testIsUsedEmail_ReturnsTrue(){
		given(mockBusinessOwnerRepo.findByEmail(testOwner.getEmail()))
				.willReturn(Optional.of(testOwner));

		boolean isUsedEmail = businessOwnerService.isUsedEmail(testOwner.getEmail());

		assertThat(isUsedEmail).isTrue();
		verify(mockBusinessOwnerRepo,times(1))
				.findByEmail(testOwner.getEmail());
	}

	@Test
	public void BusinessOwnerService_testIsUsedEmail_ReturnsFalse(){
		given(mockBusinessOwnerRepo.findByEmail(testOwner.getEmail()))
				.willReturn(Optional.empty());

		boolean isUsedEmail = businessOwnerService.isUsedEmail(testOwner.getEmail());

		assertThat(isUsedEmail).isFalse();
	}


}
