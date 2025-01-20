package com.schdlr.Service;

import com.schdlr.dto.KeyActivity;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
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

	@Test
	void TokenKeyService_testManageKeys_ManagesCorrectly() {
		TokenKey activeKey = new TokenKey();
		activeKey.setKeyActivity(KeyActivity.ACTIVE);
		activeKey.setTimeOfCreation(Instant.now().minus(91,ChronoUnit.DAYS ));

		TokenKey graceKey = new TokenKey();
		graceKey.setKeyActivity(KeyActivity.ACTIVE);
		graceKey.setTimeOfCreation(Instant.now().minus(84, ChronoUnit.DAYS));

		given(mockTokenKeyRepo.findAll()).
				willReturn(List.of(activeKey, graceKey));

		tokenKeyService.manageKeys();

		// Assert
		verify(mockTokenKeyRepo, times(1)).deleteKeyById(activeKey.getKid());
		verify(mockTokenKeyRepo, times(1))
				.save(argThat(k -> Objects.equals(k.getKid(), graceKey.getKid()) && k.getKeyActivity() == KeyActivity.GRACE));
	}

}
