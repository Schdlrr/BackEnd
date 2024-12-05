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

    /*
     * Constructor to inject the TokenKeyRepo dependency.
     * tokenKeyRepo-Repository for managing TokenKey entities.
     */
    public TokenKeyService(TokenKeyRepo tokenKeyRepo){
        this.tokenKeyRepo = tokenKeyRepo;
    }

    /**
     * A scheduled task to generate two new RSA key pairs every three weeks.
     * The keys are saved in the database with an ACTIVE state.
     * throws NoSuchAlgorithmException If the RSA algorithm is unavailable.
     */
    @Scheduled(fixedRate = 1814400000) // 3 weeks in milliseconds
    public void generateKeys() throws NoSuchAlgorithmException{
        for(int i = 0; i < 2; i ++){
            KeyPair keyPair = TokenKeyGen.genKeyPair(); // Generate a new RSA key pair
            
            // Create a new TokenKey entity
            TokenKey tokenKey = new TokenKey();
            tokenKey.setPublicKey( TokenKeyGen.encodeKeyToString(keyPair.getPublic())); // Encode and set public key
            tokenKey.setPrivateKey(TokenKeyGen.encodeKeyToString(keyPair.getPrivate())); // Encode and set private key
            tokenKey.setKeyActivity(KeyActivity.ACTIVE); // Set the key's activity status to ACTIVE

            tokenKeyRepo.save(tokenKey); // Save the TokenKey entity to the repository

        }
    }

    /*
     * A scheduled task to manage the lifecycle of keys:
     * - Keys older than 13 weeks are deleted.
     * - Keys older than 12 weeks are marked as GRACE if currently ACTIVE.
     */
    @Scheduled(fixedRate = 60480000) // 1 week in milliseconds
    public void manageKeys(){
        List<TokenKey> keys = tokenKeyRepo.findAll(); // Retrieve all keys from the database

        Instant now = Instant.now(); // Current timestamp

        for(TokenKey key : keys){
            // Calculate the number of weeks elapsed since the key's creation
            long weeksElapsed = key.getTimeOfCreation().until(now, ChronoUnit.DAYS) / 7;


            // Delete keys older than 13 weeks
            if(weeksElapsed >= 13){
                tokenKeyRepo.deleteKeyById(key.getKid());

            // Mark keys older than 12 weeks as GRACE if they are currently ACTIVE
            }else if(weeksElapsed >=12 &&  key.getKeyActivity() == KeyActivity.ACTIVE){
                key.setKeyActivity(KeyActivity.GRACE); // Update activity status to GRACE
                tokenKeyRepo.save(key); // Save the updated key back to the repository
            }
        }
    }
}
