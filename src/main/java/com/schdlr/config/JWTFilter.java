package com.schdlr.config;

import com.schdlr.service.UserDetailsConfigService;
import com.schdlr.util.TokenAndCookiesUtil;
import com.schdlr.util.TokenExtractionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final  TokenExtractionUtil tokenExtractionUtil;

    private final ApplicationContext applicationContext;

    private final TokenAndCookiesUtil tokenAndCookiesUtil;


    public JWTFilter(TokenExtractionUtil tokenExtractionUtil, ApplicationContext applicationContext
    , TokenAndCookiesUtil tokenAndCookiesUtil) {
        this.tokenExtractionUtil = tokenExtractionUtil;
        this.applicationContext = applicationContext;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String token = tokenAndCookiesUtil.extractTokenFromCookies(request, "accessToken");
        String email;
        String role;
        if (token != null) {
            try {
                

                email = tokenExtractionUtil.extractEmail(token);
                role = tokenExtractionUtil.extractRole(token);
                log.info("Email, role and token:" + email +  " " + " " + role + " " + token);

                // If email exists and no authentication is currently set, proceed
                if (email != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Retrieve user details from the UserDetailsConfigService bean
                    UserDetails userDetails = applicationContext.getBean(UserDetailsConfigService.class)
                            .loadUserByEmail(email,role);
                            log.info(userDetails.toString());

                    if (tokenExtractionUtil.authenticateToken(token)) {
                        // Create an authentication token using the retrieved user details
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                                log.info(authToken.toString());

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
