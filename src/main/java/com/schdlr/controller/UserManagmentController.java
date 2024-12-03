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
import jakarta.servlet.http.Cookie;
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
    public ResponseEntity<String> signIn(@RequestBody SignedUpUser user, HttpServletResponse response,
            HttpServletRequest request) {

        String username = user.getUserName();

        String existingAccessToken = tokenAndCookiesUtil.getCookie(request, "accessToken");
        String existingRefreshToken = tokenAndCookiesUtil.getCookie(request, "refreshToken");

        if (existingAccessToken != null && existingRefreshToken != null) {
            try {
                String s1 = service.verify(request, response);

                if (s1.equals(username)) {
                    return service.userSignIn(user);
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithm bucko");
            } catch (InvalidKeySpecException w) {
                System.out.println("Wrong key spec");
            }
        }
        try {
            String refreshToken = jwtService.generateRefreshToken(username);
            String accessToken = jwtService.generateAccessToken(username);
            tokenAndCookiesUtil.addCookie(response, "accessToken", accessToken, 30 * 60, true, false, "Strict");
            tokenAndCookiesUtil.addCookie(response, "refreshToken", refreshToken, 30 * 24 * 60 * 60, true, false,
                    "Strict"); // 30 days expiration
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
        String s = service.verify(request, response);
        if (s.startsWith("Out of use refresh token")) {
            return new ResponseEntity<>(s, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }

    }

}
