package com.schdlr.controller;

import com.schdlr.model.BusinessOwner;
import com.schdlr.service.BusinessOwnerService;
import com.schdlr.service.JWTService;
import com.schdlr.util.TokenAndCookiesUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Slf4j
@RestController
@RequestMapping("/api/business")
@CrossOrigin(origins = "http://localhost:3000")
public class BusinessOwnerController{

    private BusinessOwnerService businessOwnerService;

    private JWTService jwtService;

    private TokenAndCookiesUtil tokenAndCookiesUtil;

    public BusinessOwnerController(BusinessOwnerService businessOwnerService,
    JWTService jwtService , TokenAndCookiesUtil tokenAndCookiesUtil){
        this.businessOwnerService = businessOwnerService;
        this.jwtService = jwtService;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
    }

    @GetMapping("/test")
    public String testingMethod(@RequestParam String param) {
        return "Welcome to the best website ever";
    }
    

    
     @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody BusinessOwner BO) {
        return businessOwnerService.userSignUp(BO);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> userSignIn(@RequestBody BusinessOwner BO, HttpServletResponse response) {
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

        return businessOwnerService.userSignIn(BO);
    }

}
