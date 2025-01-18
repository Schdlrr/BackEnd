package com.schdlr.Service;

import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;
import com.schdlr.service.SignedUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Testable
@ExtendWith(MockitoExtension.class)
public class BusinessOwnerServiceTest {

	@Mock
	private BusinessOwnerRepo mockBusinessOwnerRepo;

	@Mock
	private BCryptPasswordEncoder mockEncoder;

	@InjectMocks
	private SignedUserService signedUserService;

	private BusinessOwner testOwner;

	@BeforeEach
	public void setUp(){
		testOwner = BusinessOwner.builder().userName("testOwner")
				.email("erdisyla6@gmail.com").password("password")
				.number("044221100").businessAddress("businessAddress")
				.businessName("businessName").build();
	}



}
