package com.schdlr.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.schdlr.model.SignedUpUser;
import com.schdlr.service.JWTService;
import com.schdlr.service.UserManagmentService;
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

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserManagmentController {

    private UserManagmentService service;
    private final JWTService jwtService;
    private final TokenAndCookiesUtil tokenAndCookiesUtil;

    public UserManagmentController(UserManagmentService service, JWTService jwtService,
            TokenAndCookiesUtil tokenAndCookiesUtil) {
        this.service = service;
        this.jwtService = jwtService;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
    }

    @Hidden
    @GetMapping("/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody SignedUpUser user) {
        return service.userSignUp(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> userSignIn(@RequestBody SignedUpUser user, HttpServletResponse response,
            HttpServletRequest request) {
        String username = user.getUserName();
        try {
            String refreshToken = jwtService.generateRefreshToken(username);
            String accessToken = jwtService.generateAccessToken(username);
            tokenAndCookiesUtil.addCookie(response, "accessToken", accessToken, 30 * 60, true, false, "Strict");
            tokenAndCookiesUtil.addCookie(response, "refreshToken", refreshToken, 30 * 24 * 60 * 60, true, false,
                    "Strict");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithm bucko");
        } catch (InvalidKeySpecException w) {
            System.out.println("Wrong key spec");
        }

        return service.userSignIn(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String existingRefreshToken = tokenAndCookiesUtil.extractTokenFromCookies(request, "refreshToken");

        if (existingRefreshToken != null) {
            try {
                String s1 = service.verify(request, response);

                if (s1.equals(jwtService.extractUsername(existingRefreshToken))) {
                    return new ResponseEntity<>(s1, HttpStatus.ACCEPTED);
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithm bucko");
            } catch (InvalidKeySpecException w) {
                System.out.println("Wrong key spec");
            }
        }
        return new ResponseEntity<>("Refresh token is invalid. User needs to sign in", HttpStatus.BAD_REQUEST);
    }

}
