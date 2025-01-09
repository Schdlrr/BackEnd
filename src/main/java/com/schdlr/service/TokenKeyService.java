package com.schdlr.service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.schdlr.dto.KeyActivity;
import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;
import com.schdlr.util.TokenExtractionUtil;
import com.schdlr.util.TokenKeyGen;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/*
 * TokenKeyService handles scheduled requests related to
 * TokenKey creation and managment.
 * 
 * Responsibilities:
 * - Creating refresh and sign-in tokens.
 * - Deleting TokenKeys when they expire.
 * - Marking them with grace when they enter
 * the period where they should only validate exisiting tokens
 * not sign them.
 * Annotations:
 * - @Service: Marks this class as a service
 */
@Slf4j
@Service
public class TokenKeyService {


    private TokenKeyRepo tokenKeyRepo;

    private TokenExtractionUtil tokenExtractionUtil;

    public TokenKeyService(TokenKeyRepo tokenKeyRepo, TokenExtractionUtil tokenExtractionUtil){
        this.tokenKeyRepo = tokenKeyRepo;
        this.tokenExtractionUtil = tokenExtractionUtil;
    }

    @Scheduled(fixedRate = 1814400000)
    public void generateKeys() throws NoSuchAlgorithmException{
        for(int i = 0; i < 2; i ++){
            KeyPair keyPair = TokenKeyGen.genKeyPair();
            
            // Create a new TokenKey entity
            TokenKey tokenKey = new TokenKey();
            tokenKey.setPublicKey( TokenKeyGen.encodeKeyToString(keyPair.getPublic()));
            tokenKey.setPrivateKey(TokenKeyGen.encodeKeyToString(keyPair.getPrivate()));
            tokenKey.setKeyActivity(KeyActivity.ACTIVE);

            tokenKeyRepo.save(tokenKey);
            log.info("New keys created");

        }
        tokenExtractionUtil.refreshKeys();
    }

    @Scheduled(fixedRate = 60480000)
    public void manageKeys(){
        List<TokenKey> keys = tokenKeyRepo.findAll();

        Instant now = Instant.now();

        for(TokenKey key : keys){
            long weeksElapsed = key.getTimeOfCreation().until(now, ChronoUnit.DAYS) / 7;

            if(weeksElapsed >= 13){
                tokenKeyRepo.deleteKeyById(key.getKid());

            }else if(weeksElapsed >=12 &&  key.getKeyActivity() == KeyActivity.ACTIVE){
                key.setKeyActivity(KeyActivity.GRACE);
                tokenKeyRepo.save(key);
            }
        }
        log.info("Weekly check done");
    }
}
