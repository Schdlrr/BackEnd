package com.schdlr.config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.schdlr.service.UserDetailsConfigService;
import com.schdlr.util.TokenAndCookiesUtil;
import com.schdlr.util.TokenExtractionUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * JWTFilter is a custom filter that intercepts incoming HTTP requests
 * to validate and process JSON Web Tokens (JWTs) for authentication.
 * 
 * This filter:
 * - Excludes certain paths from JWT validation (e.g., login and signup
 * endpoints).
 * - Extracts the JWT from cookies in the request.
 * - Validates the token and sets the security context if valid.
 * 
 * Extends OncePerRequestFilter to ensure it is executed once per request.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    // Util responsible for handling JWT operations like extraction and
    // validation
    private final  TokenExtractionUtil tokenExtractionUtil;

    // Spring's application context for dynamically retrieving beans
    private final ApplicationContext applicationContext;

    //Util for extracting tokens out of cookies
    private final TokenAndCookiesUtil tokenAndCookiesUtil;

    /*
     * jwtService - Service for handling JWT-related operations.
     * applicationContext - Spring application context for accessing beans.
     */
    public JWTFilter(TokenExtractionUtil tokenExtractionUtil, ApplicationContext applicationContext
    , TokenAndCookiesUtil tokenAndCookiesUtil) {
        this.tokenExtractionUtil = tokenExtractionUtil;
        this.applicationContext = applicationContext;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;

    }

    /*
     * Core filtering logic for processing JWT authentication.
     * 
     * request - The HTTP request object.
     * response - The HTTP response object.
     * filterChain - The filter chain to pass the request/response to the next
     * filter.
     * ServletException - If a servlet error occurs.
     * IOException - If an I/O error occurs during filtering.
     * 
     * Logic:
     * - Skips processing for excluded paths.
     * - Extracts JWT from cookies and validates it.
     * - Sets the authenticated user in the security context if the token is valid.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Define paths that should bypass JWT validation (endpoints that should be
        // accessible to all users)
        String[] excludedPaths = { "/api/user/signin", "/api/user/signup", "/api/refresh-token" };

        String requestURI = request.getRequestURI();

        for (String path : excludedPaths) {
            if (requestURI.startsWith(path)) {
                filterChain.doFilter(request, response); // Continue without filtering
                return;
            }
        }

        String token = tokenAndCookiesUtil.extractTokenFromCookies(request, "accessToken");
        String email;
        if (token != null) {
            try {

                email = tokenExtractionUtil.extractEmail(token);

                // If email exists and no authentication is currently set, proceed
                if (email != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Retrieve user details from the UserDetailsConfigService bean
                    UserDetails userDetails = applicationContext.getBean(UserDetailsConfigService.class)
                            .loadUserByEmail(email);

                    if (tokenExtractionUtil.authenticateToken(token)) {
                        // Create an authentication token using the retrieved user details
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Attach additional request-specific detail
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set the authentication token in the security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

}
