package com.schdlr.Service;

import com.schdlr.dto.KeyActivity;
import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;
import com.schdlr.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Testable
@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

	@Mock
	private TokenKeyRepo tokenKeyRepo;

	@InjectMocks
	private JWTService jwtService;

	private TokenKey testKey;
	private String role;
	private String email;

	@BeforeEach
	public void setUp(){
		testKey = TokenKey.builder().publicKey("Public Key")
				.privateKey("Private Key").keyActivity(KeyActivity.ACTIVE)
				.build();
		role = "role";
		email = "email";
	}
}
