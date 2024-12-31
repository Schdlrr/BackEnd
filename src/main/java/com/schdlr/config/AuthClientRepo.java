/* package com.schdlr.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthClientRepo implements OAuth2AuthorizedClientRepository {

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(
            String clientRegistrationId, Authentication principal, HttpServletRequest request) {
            
    }

    @Override
    public void saveAuthorizedClient(
            OAuth2AuthorizedClient authorizedClient, Authentication principal, HttpServletRequest request,
            HttpServletResponse response) {
        // Save the client information in a stateless manner (e.g., JWT or headers)
    }

    @Override
    public void removeAuthorizedClient(
            String clientRegistrationId, Authentication principal, HttpServletRequest request,
            HttpServletResponse response) {
        // Remove client info if needed
    }
} */
