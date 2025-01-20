package com.schdlr.Service;

import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;
import com.schdlr.service.TokenKeyService;
import com.schdlr.util.TokenExtractionUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Testable
@ExtendWith(MockitoExtension.class)
public class TokenKeyServiceTest {

	@Mock
	private TokenKeyRepo mockTokenKeyRepo;

	@Mock
	private TokenExtractionUtil mockTokenExtractionUtil;

	@InjectMocks
	private TokenKeyService tokenKeyService;



	@Test
	public void TokenKeyService_testGenerateKey_GeneratesKey() throws NoSuchAlgorithmException {

		tokenKeyService.generateKeys();

		verify(mockTokenKeyRepo, times(2)).save(any(TokenKey.class));
		verify(mockTokenExtractionUtil, times(1)).refreshKeys();
	}

}
