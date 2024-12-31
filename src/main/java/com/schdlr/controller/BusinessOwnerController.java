package com.schdlr.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.schdlr.model.BusinessOwner;
import com.schdlr.service.BusinessOwnerService;
import com.schdlr.service.JWTService;
import com.schdlr.util.TokenAndCookiesUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/business")
@CrossOrigin(origins = "http://localhost:3000")
public class BusinessOwnerController{

    private BusinessOwnerService bussinesOwnerService;

    private JWTService jwtService;

    private TokenAndCookiesUtil tokenAndCookiesUtil;

    public BusinessOwnerController(BusinessOwnerService bussinesOwnerService, 
    JWTService jwtService , TokenAndCookiesUtil tokenAndCookiesUtil){
        this.bussinesOwnerService = bussinesOwnerService;
        this.jwtService = jwtService;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
    }

    @GetMapping("/test")
    public String testingMethod(@RequestParam String param) {
        return "Welcome to the best website ever";
    }
    

    
     @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody BusinessOwner BO) {
        return bussinesOwnerService.userSignUp(BO);
    }


     /*
     * Handles user signin requests and generates authentication tokens.
     * 
     * Endpoint: POST /api/user/signin
     * 
     * Process:
     * - Validates user credentials.
     * - Generates access and refresh tokens for the user.
     * - Stores tokens in secure HTTP-only cookies.
     * 
     * user - The user object containing signin credentials (username and password).
     * response - HttpServletResponse for adding cookies to the response.
     * request - HttpServletRequest for processing the HTTP request.
     * returns ResponseEntity<String> - Response indicating success or failure of the signin process.
     */
    @PostMapping("/signin")
    public ResponseEntity<String> userSignIn(@RequestBody BusinessOwner BO, HttpServletResponse response,
            HttpServletRequest request) {
        String email = BO.getEmail();
        try {
            // Generate tokens for the user
            String refreshToken = jwtService.generateRefreshToken(email,"BusinessOwner");
            String accessToken = jwtService.generateAccessToken(email,"BusinessOwner");

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

        return bussinesOwnerService.userSignIn(BO);
    }

}
