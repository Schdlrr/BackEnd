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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

}
