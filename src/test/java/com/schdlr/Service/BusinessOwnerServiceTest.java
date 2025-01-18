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

		ResponseEntity<String> response = businessOwnerService.userSignUp(testOwner);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo("testOwner");
		verify(mockEncoder,times(1)).encode("password");
		verify(mockBusinessOwnerRepo, times(1)).save(testOwner);

	}

	@Test
	public void BusinessOwnerService_testSignUp_EmailIsNotValid(){
		testOwner.setEmail("this is not a valid email");

		ResponseEntity<String> response = businessOwnerService.userSignUp(testOwner);

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

		ResponseEntity<String> response = businessOwnerService.userSignUp(testOwner);

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


		ResponseEntity<String> response = businessOwnerService.userSignIn(testOwner);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("testOwner");
		verify(mockBusinessOwnerRepo, times(1)).findByEmail("erdisyla6@gmail.com");
		verify(mockEncoder,times(1))
				.matches(testOwner.getPassword(),testOwner.getPassword());

	}
}
