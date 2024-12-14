package com.schdlr.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.NoSuchElementException;

import com.schdlr.model.SignedUser;
import com.schdlr.repo.UserManagmentRepo;
import com.schdlr.util.TokenExtractionUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenManagmentService {

    private final TokenExtractionUtil tokenExtractionUtil;

    private final UserManagmentRepo repo;

    public TokenManagmentService (UserManagmentRepo repo , TokenExtractionUtil tokenExtractionUtil){
        this.repo = repo;
        this.tokenExtractionUtil = tokenExtractionUtil;
    }

    /*
     * Verifies a refresh token extracted from cookies.
     * request-The HttpServletRequest containing the cookies.
     * response-The HttpServletResponse to send responses.
     * returns The username if the token is valid, or an error message otherwise.
     */
    public ResponseEntity<String> verifyRefreshToken( HttpServletRequest request, HttpServletResponse response,String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(!tokenExtractionUtil.authenticateToken(refreshToken)){
            return new ResponseEntity<>("Refresh Token is invalid", HttpStatus.UNAUTHORIZED);
        }
        String email = tokenExtractionUtil.extractEmail(refreshToken);
        SignedUser user;
        try{
        user = repo.findByEmail(email).get();
        }catch(NoSuchElementException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        log.info("User verified");
        return new ResponseEntity<>(user.getUserName(), HttpStatus.OK);
    }
}
