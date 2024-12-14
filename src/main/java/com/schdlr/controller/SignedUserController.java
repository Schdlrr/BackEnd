package com.schdlr.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.schdlr.model.SignedUser;
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
import lombok.extern.slf4j.Slf4j;

/*
 * UserManagmentController handles API requests related to user management.
 * 
 * Responsibilities:
 * - User signup and signin functionality.
 * - Token-based authentication and refresh token management.
 * - Redirect to the Swagger documentation page.
 * - CORS configuration to allow frontend interaction.
 * 
 * Annotations:
 * - @RestController: Marks this class as a REST controller for handling HTTP requests.
 * - @RequestMapping("/api/user"): Maps all endpoints in this controller to the "/api/user" path.
 * - @CrossOrigin(origins = "http://localhost:3000"): Enables Cross-Origin Resource Sharing (CORS) 
 *   for requests from the frontend.
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class SignedUserController {

    // Service for handling business logic related to user management
    private UserManagmentService service;

    // Service for generating and validating JWT tokens
    private final JWTService jwtService;

    // Utility class for working with cookies and tokens
    private final TokenAndCookiesUtil tokenAndCookiesUtil;

     /*
     * Constructor for injecting required dependencies.
     * 
     * service - The UserManagmentService for business logic.
     * jwtService - The JWTService for token generation and validation.
     * tokenAndCookiesUtil - Utility for managing tokens and cookies.
     */
    public SignedUserController(UserManagmentService service, JWTService jwtService,
            TokenAndCookiesUtil tokenAndCookiesUtil) {
        this.service = service;
        this.jwtService = jwtService;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
    }

    /*
     * Redirects root requests to the Swagger documentation page.
     * 
     * Endpoint: GET /api/user/
     * 
     * response - HttpServletResponse object for sending the redirect response.
     * IOException - If an input/output error occurs during redirection.
     */
    @Hidden
    @GetMapping("/")
    public void redirect(HttpServletResponse response) throws IOException {
        // Redirects to Swagger documentation when accessing root endpoint
        response.sendRedirect("/swagger-ui/index.html");
    }

    /*
     * Handles user signup requests.
     * 
     * Endpoint: POST /api/user/signup
     * 
     * user - The user object containing signup information (username, password, etc.).
     * returns ResponseEntity<String> - Response indicating success or failure of the signup process.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody SignedUser user) {
        return service.userSignUp(user);
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
