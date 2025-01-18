package com.schdlr.Service;

import com.schdlr.model.SignedUser;
import com.schdlr.repo.SignedUserRepo;
import com.schdlr.service.SignedUserService;
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


@Testable
@ExtendWith(MockitoExtension.class)
public class SignedUserServiceTest {

	@Mock
	private SignedUserRepo mockSignedUserRepo;

	@Mock
	private BCryptPasswordEncoder mockEncoder;

	@InjectMocks
	private SignedUserService signedUserService;

	private SignedUser testUser;

	@BeforeEach
	public void setUp(){
		testUser = SignedUser.builder().userName("testUser")
				.email("erdisyla6@gmail.com").password("password")
				.number("044221100").build();
	}

	@Test
	public void SignedUserService_testSignUp_ReturnsCorrectResponse(){
		given(mockEncoder.encode(testUser.getPassword()))
				.willReturn("encodedPassword");
		given(mockSignedUserRepo.save(testUser))
				.willReturn(testUser);

		ResponseEntity<String> response = signedUserService.userSignUp(testUser);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo("testUser");
		verify(mockSignedUserRepo, times(1)).save(testUser);

	}

	@Test
	public void SignedUserService_testSignUp_EmailIsNotValid(){
		testUser.setEmail("this is not a valid email");

		ResponseEntity<String> response = signedUserService.userSignUp(testUser);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
		assertThat(response.getBody()).isEqualTo("Non valid email format entered.Please change email");
		verifyNoInteractions(mockEncoder);
		verifyNoInteractions(mockSignedUserRepo);
	}

	@Test
	public void SignedUserService_testSignUp_EmailIsAlreadyInUse(){
		given(mockSignedUserRepo.findByEmail(testUser.getEmail()))
				.willReturn(Optional.of(testUser));

		ResponseEntity<String> response = signedUserService.userSignUp(testUser);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(response.getBody())
				.isEqualTo("Email is already used by another user. Please try to sign up with another kind of contact info");
		verifyNoInteractions(mockEncoder);
	}

	@Test
	public void SignedUserService_testSignIn_ReturnsCorrectResponse(){
		given(mockSignedUserRepo.findByEmail("erdisyla6@gmail.com"))
				.willReturn(Optional.of(testUser));
		given(mockEncoder.matches(testUser.getPassword(),testUser.getPassword()))
				.willReturn(true);


		ResponseEntity<String> response = signedUserService.userSignIn(testUser);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("testUser");
		verify(mockSignedUserRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verify(mockEncoder,times(1))
				.matches(testUser.getPassword(),testUser.getPassword());

	}

	@Test
	public void SignedUserService_testSignIn_ReturnsBadRequest(){
		given(mockSignedUserRepo.findByEmail(testUser.getEmail()))
				.willReturn(Optional.empty());

		ResponseEntity<String> response = signedUserService.userSignIn(testUser);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isEqualTo("User not present in database");
		verify(mockSignedUserRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verifyNoInteractions(mockEncoder);

	}

	@Test
	public void SignedUserService_testSignIn_ReturnsUnauthorized(){
		given(mockSignedUserRepo.findByEmail(testUser.getEmail()))
				.willReturn(Optional.of(testUser));
		given(mockEncoder.matches(testUser.getPassword(),testUser.getPassword()))
				.willReturn(false);

		ResponseEntity<String> response = signedUserService.userSignIn(testUser);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).isEqualTo("The credentials that were used do not match to any user please try again");
		verify(mockSignedUserRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verify(mockEncoder,times(1))
				.matches(testUser.getPassword(),testUser.getPassword());
	}

	@Test
	public void SignedUserService_testIsValidEmail_ReturnsTrue(){
		boolean isValidEmail = signedUserService.isValidEmail(testUser.getEmail());

		assertThat(isValidEmail).isTrue();
	}

	@Test
	public void SignedUserService_testIsValidEmail_ReturnsFalse(){
		boolean isValidEmail = signedUserService.isValidEmail("invalidemail.com");

		assertThat(isValidEmail).isFalse();
	}

	@Test
	public void SignedUserService_testIsUsedEmail_ReturnsTrue(){
		given(mockSignedUserRepo.findByEmail(testUser.getEmail()))
				.willReturn(Optional.of(testUser));

		boolean isUsedEmail = signedUserService.isUsedEmail(testUser.getEmail());

		assertThat(isUsedEmail).isTrue();
	}

	@Test
	public void SignedUserService_testIsUsedEmail_ReturnsFalse(){
		given(mockSignedUserRepo.findByEmail(testUser.getEmail()))
				.willReturn(Optional.empty());

		boolean isUsedEmail = signedUserService.isUsedEmail(testUser.getEmail());

		assertThat(isUsedEmail).isFalse();
	}
}
