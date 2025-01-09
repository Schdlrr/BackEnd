package com.schdlr.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.schdlr.model.SignedUser;
import com.schdlr.service.JWTService;
import com.schdlr.service.SignedUserService;
import com.schdlr.util.TokenAndCookiesUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class SignedUserController{

    private SignedUserService service;

    private JWTService jwtService;

    private TokenAndCookiesUtil tokenAndCookiesUtil;

    public SignedUserController(SignedUserService service, JWTService jwtService,
    TokenAndCookiesUtil tokenAndCookiesUtil){
        this.service = service;
        this.jwtService = jwtService;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
    }
    
    @Hidden
    @GetMapping("/")
    public void redirect(HttpServletResponse response) throws IOException {
        // Redirects to Swagger documentation when accessing root endpoint
        response.sendRedirect("/swagger-ui/index.html");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody SignedUser user) {
        return service.userSignUp(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> userSignIn(@RequestBody SignedUser user, HttpServletResponse response,
            HttpServletRequest request) {
        String email = user.getEmail();
        try {
            // Generate tokens for the user
            String refreshToken = jwtService.generateRefreshToken(email,"SignedUser");
            String accessToken = jwtService.generateAccessToken(email,"SignedUser");

            // Add tokens as cookies in the response
            tokenAndCookiesUtil.addCookie(response, "accessToken", accessToken, 30 * 60, true, false, "Strict");
            tokenAndCookiesUtil.addCookie(response, "refreshToken", refreshToken, 30 * 24 * 60 * 60, true, false,
                    "Strict");
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm for token does not exist", e);
            return new ResponseEntity<>("Algorithm for token does not exist", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidKeySpecException w) {
            log.error("The key sppec used is invalid", w);
            return new ResponseEntity<>("The key sppec used is invalid" , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return service.userSignIn(user);
    }

}
