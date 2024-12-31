package com.schdlr.service;


import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

import com.schdlr.model.SignedUser;
import com.schdlr.model.UserPrincipal;
import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;
import com.schdlr.repo.UserManagmentRepo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/*
 * UserDetailsConfigService handles API requests related to loading users by
 * username and email.
 * 
 * Responsibilities:
 * - Loading users by their username.
 * - Loading users by their email
 * 
 * Annotations:
 * - @Service: Marks this class as a service
 */

@Slf4j
@Service
public class UserDetailsConfigService  implements UserDetailsService{

    private UserManagmentRepo userManagmentRepo;
    private BusinessOwnerRepo businessOwnerRepo;

    /**
     * Constructor for injecting the UserManagmentRepo dependency.
     * userManagmentRepo-Repository for user management operations.
     */
    public UserDetailsConfigService(UserManagmentRepo userManagmentRepo, BusinessOwnerRepo businessOwnerRepo){
        this.userManagmentRepo = userManagmentRepo;
        this.businessOwnerRepo = businessOwnerRepo;
    }


    /*
     * Loads a user by their username. Throws an exception if the user is not found.
     * username-The username to search for.
     * returns UserDetails containing the user's information.
     * throws UsernameNotFoundException If no user with the given username exists.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<SignedUser> user = userManagmentRepo.findByUserName(username); // Fetch user from the repository

        // Check if the user is present; otherwise, throw an exception
        if(!user.isPresent()){
            System.out.println("User not found"); // Log message for debugging  
            throw new UsernameNotFoundException(username); // Throw exception
        }

        return new UserPrincipal(user.get()); // Wrap the user in a UserPrincipal object
    }


    public UserDetails loadUserByEmail(String email, String role) throws UserPrincipalNotFoundException {
        if ("BusinessOwner".equals(role)) {
            return businessOwnerRepo.findByEmail(email)
                    .map(UserPrincipal::new)
                    .orElseThrow(() -> {
                        log.info("User is not a business owner");
                        return new UserPrincipalNotFoundException(email);
                    });
        }
    
        if ("SignedUser".equals(role)) {
            return userManagmentRepo.findByEmail(email)
                    .map(UserPrincipal::new)
                    .orElseThrow(() -> {
                        log.info("User is not a signed user");
                        return new UserPrincipalNotFoundException(email);
                    });
        }
    
        throw new IllegalArgumentException("Invalid role provided: " + role);
    }

}
