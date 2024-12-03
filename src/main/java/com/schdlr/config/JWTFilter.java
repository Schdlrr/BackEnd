package com.schdlr.config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import com.schdlr.service.JWTService;
import com.schdlr.service.UserDetailsConfigService;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

    private JWTService jwtService;

    private ApplicationContext applicationContext;

    public JWTFilter(JWTService jwtService, ApplicationContext applicationContext){
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String[] excludedPaths = { "/api/user/signin", "/api/user/signup", "/api/user/refresh-token"};

        String requestURI = request.getRequestURI();

        // Skip JWT processing if the request URI matches an excluded path
        for (String path : excludedPaths) {
            if (requestURI.startsWith(path)) {
                filterChain.doFilter(request, response); // Continue without filtering
                return;
            }
        }
        String token = jwtService.extractTokenFromCookies(request, "accessToken");
        String username;
        if (token != null) {
            try {
                username = jwtService.extractUsername(token);

                if (username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                            UserDetails userDetails = applicationContext.getBean(UserDetailsConfigService.class).loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }

    

} 
