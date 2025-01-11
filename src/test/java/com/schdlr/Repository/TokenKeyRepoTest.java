package com.schdlr.Repository;

import com.schdlr.dto.KeyActivity;
import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testable
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TokenKeyRepoTest {

    private TokenKeyRepo tokenKeyRepo;

    private TokenKey testKey;
    private TokenKey testKey2;
    private TokenKey testKey3;

    public TokenKeyRepoTest(@Autowired TokenKeyRepo tokenKeyRepo){
        this.tokenKeyRepo = tokenKeyRepo;
    }

    @BeforeEach
    public void setUp(){
       testKey = TokenKey.builder().publicKey("Public Key 1")
                .privateKey("Private Key 1").keyActivity(KeyActivity.ACTIVE)
                .timeOfCreation(Instant.now()).build();
       testKey2 = TokenKey.builder().publicKey("Public Key 2")
                .privateKey("Private Key 2").keyActivity(KeyActivity.ACTIVE)
                .timeOfCreation(Instant.now()).build();
       testKey3 = TokenKey.builder().publicKey("Public Key 3")
                .privateKey("Private Key 3").keyActivity(KeyActivity.ACTIVE)
                .timeOfCreation(Instant.now()).build();
    }

    @Test
    public void TokenKeyRepo_testSave_ReturnsTokenKey(){

        TokenKey savedKey = tokenKeyRepo.saveAndFlush(testKey);

        assertThat(savedKey).isNotNull();
        assertThat(savedKey.getPublicKey()).isEqualTo("Public Key 1");
        assertThat(savedKey.getPrivateKey()).isEqualTo("Private Key 1");
        assertThat(savedKey.getKeyActivity()).isEqualTo(KeyActivity.ACTIVE);
        assertThat(savedKey.getKid()).isEqualTo(testKey.getKid());
    }

    @Test
    public void TokenKeyRepo_testDeleteKeyById_ReturnsVoid(){

        TokenKey savedKey = tokenKeyRepo.saveAndFlush(testKey);

        tokenKeyRepo.deleteKeyById(testKey.getKid());
        Optional<TokenKey> emptyOptional = tokenKeyRepo.findById(testKey.getKid());

        assertThat(emptyOptional).isEmpty();
    }

    @Test
    public void TokenKeyRepo_testFindAllActiveKeys_ReturnsListOfKeys(){
        List<TokenKey> expectedListOfKeys = Arrays.asList(testKey,testKey2,testKey3);

        tokenKeyRepo.saveAndFlush(testKey);
        tokenKeyRepo.saveAndFlush(testKey2);
        tokenKeyRepo.saveAndFlush(testKey3);

        List<TokenKey> actualListOfKeys = tokenKeyRepo.findAllActiveKeys();

        assertThat(actualListOfKeys).isNotNull();
        assertThat(actualListOfKeys).isEqualTo(expectedListOfKeys);
    }


    @Test
    public void TokenKeyRepo_testFindKeyById_ReturnsKey(){
        TokenKey expectedKey = tokenKeyRepo.saveAndFlush(testKey);

        TokenKey actualKey = tokenKeyRepo.findById(expectedKey.getKid()).get();

        assertThat(actualKey).isNotNull();
        assertThat(actualKey.getKid()).isEqualTo(expectedKey.getKid());
        assertThat(actualKey.getPrivateKey()).isEqualTo(expectedKey.getPrivateKey());
        assertThat(actualKey.getPublicKey()).isEqualTo(expectedKey.getPublicKey());
        assertThat(actualKey.getKeyActivity()).isEqualTo(expectedKey.getKeyActivity());
    }
}
