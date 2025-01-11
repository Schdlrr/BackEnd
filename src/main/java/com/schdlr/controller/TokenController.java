package com.schdlr.controller;

import com.schdlr.service.TokenManagmentService;
import com.schdlr.util.TokenAndCookiesUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/token")
@CrossOrigin(origins = "http://localhost:3000")
public class TokenController {

    private TokenAndCookiesUtil tokenAndCookiesUtil;

    private  TokenManagmentService tokenManagmentService;

    public TokenController(TokenAndCookiesUtil tokenAndCookiesUtil,
                           TokenManagmentService tokenManagmentService){

        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
        this.tokenManagmentService = tokenManagmentService;

    }

        @PostMapping("/refresh")
        public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeySpecException{
    
            String existingRefreshToken = tokenAndCookiesUtil.extractTokenFromCookies(request, "refreshToken");

        if (existingRefreshToken != null) {
                return tokenManagmentService.verifyRefreshToken(request, response,existingRefreshToken);
        }
        return new ResponseEntity<>("Refresh Token does not exist" , HttpStatus.BAD_REQUEST);
    }

}
