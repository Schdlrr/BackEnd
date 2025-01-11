package com.schdlr.service;

import com.schdlr.util.TokenAndCookiesUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Slf4j
@Service
public class GuestUserService {

    private JWTService jwtService;

    private TokenAndCookiesUtil tokenAndCookiesUtil;

    public GuestUserService(JWTService jwtService, TokenAndCookiesUtil tokenAndCookiesUtil){
        this.jwtService = jwtService;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
    }

    public ResponseEntity<String> signIn(HttpServletResponse response){
        try {
            String refreshToken = jwtService.generateRefreshToken(returnSecureRandomString(),"GuestUser");
            String accessToken = jwtService.generateAccessToken(returnSecureRandomString(),"GuestUser");

            tokenAndCookiesUtil.addCookie(response, "accessToken", accessToken, 30 * 60, true, false, "Strict");
            tokenAndCookiesUtil.addCookie(response, "refreshToken", refreshToken, 30 * 24 * 60 * 60, true, false,
                    "Strict");
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm for token does not exist", e);
            return new ResponseEntity<>("Algorithm for token does not exist", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidKeySpecException w) {
            log.error("The key spec used is invalid", w);
            return new ResponseEntity<>("The key sppec used is invalid" , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Guest user signed in");
        return new ResponseEntity<>("Guest user signed in" ,HttpStatus.OK);
    }

    public String returnSecureRandomString(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32]; 
        secureRandom.nextBytes(randomBytes);
        String base64String = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return base64String;
    }



}
