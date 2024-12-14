package com.schdlr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;


/*
 * SecurityConfig class is responsible for configuring Spring Security
 * for the application. It defines security policies, authentication mechanisms, 
 * and JWT filter integration to secure endpoints.
 * 
 * Key Features:
 * - Configures endpoint permissions (e.g., public and protected routes).
 * - Integrates JWT-based authentication using the custom JWTFilter.
 * - Disables session-based authentication in favor of stateless JWT authentication.
 * - Configures CORS policies for client-server communication.
 */

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    // Custom implementation of UserDetailsService for fetching user details (UserDetailsConfigService behind the covers)
    private UserDetailsService userDetailsService;

    private JWTFilter jwtFilter;


    /*
     * Configures the security filter chain for the application.
     * 
     * http - The HttpSecurity object to configure security settings.
     * SecurityFilterChain - The configured filter chain.
     * Exception - If an error occurs during configuration.
     * 
     * Key Configurations:
     * - Disables CSRF (since JWT handles security).
     * - Defines public and protected endpoint access.
     * - Adds the custom JWTFilter to the filter chain before the username-password filter.
     * - Configures session management as stateless (no server-side session storage).
     * - Configures headers to allow embedding resources from the same origin.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http.csrf(customizer -> customizer.disable())
        .authorizeHttpRequests(request -> request
        //Endpoints that should be accessible to all of the application 
        .requestMatchers("/api/user/signup", "/api/user/signin", "/api/user/"
        ,"/swagger-ui/**","/v3/api-docs/**","/api/refresh-token")
        .permitAll()
        .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
        .build();
    }

    /*
     * Configures the authentication provider for the application.
     * 
     * This method sets:
     * - The user details service for loading user information.
     * - The password encoder for securely hashing passwords (BCrypt with strength 12).
     * 
     * returns A configured AuthenticationProvider bean for authenticating users.
     */

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /*
     * Configures the authentication manager for the application.
     * 
     * This method retrieves the AuthenticationManager bean from Spring's
     * AuthenticationConfiguration, which is used for authenticating users.
     * 
     * config - The AuthenticationConfiguration object.
     * A configured AuthenticationManager bean.
     * Exception - If an error occurs during configuration.
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    /*
     * Configures Cross-Origin Resource Sharing (CORS) for the application.
     * 
     * This method allows the frontend (running on a different domain) to
     * access the backend API.
     * 
     * Configuration:
     * - Allows requests from "http://localhost:3000" (e.g., frontend).
     * - Permits specific HTTP methods (GET, POST, PUT, DELETE, OPTIONS).
     * - Allows all headers and credentials.
     * 
     * returns A WebMvcConfigurer bean for configuring CORS mappings.
     */
     @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all paths
                        .allowedOrigins("http://localhost:3000") // Allow requests from the frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specified methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials
            }
        };
    }


}
