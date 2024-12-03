package com.schdlr.service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.schdlr.model.KeyActivity;
import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenKeyService {

    private TokenKeyRepo tokenKeyRepo;

    public TokenKeyService(TokenKeyRepo tokenKeyRepo){
        this.tokenKeyRepo = tokenKeyRepo;
    }

    @Scheduled(fixedRate = 1814400000)
    public void generateKeys() throws NoSuchAlgorithmException{
        for(int i = 0; i < 2; i ++){
            KeyPair keyPair = TokenKeyGen.genKeyPair();
            

            TokenKey tokenKey = new TokenKey();
            tokenKey.setPublicKey( TokenKeyGen.encodeKeyToString(keyPair.getPublic()));
            tokenKey.setPrivateKey(TokenKeyGen.encodeKeyToString(keyPair.getPrivate()));
            tokenKey.setKeyActivity(KeyActivity.ACTIVE);

            tokenKeyRepo.save(tokenKey);

        }
    }

    @Scheduled(fixedRate = 60480000)
    public void manageKeys(){
        List<TokenKey> keys = tokenKeyRepo.findAll();

        Instant now = Instant.now();

        for(TokenKey key : keys){
            long weeksElapsed = key.getTimeOfCreation().until(now, ChronoUnit.DAYS) / 7;


            if(weeksElapsed >= 13){
                tokenKeyRepo.deleteOldKeys(key.getKid());
            }else if(weeksElapsed >=12 &&  key.getKeyActivity() == KeyActivity.ACTIVE){
                key.setKeyActivity(KeyActivity.GRACE);
                tokenKeyRepo.save(key);
            }
        }
    }
}
