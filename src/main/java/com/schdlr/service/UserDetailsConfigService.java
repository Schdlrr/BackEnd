package com.schdlr.service;


import java.util.Optional;

import com.schdlr.model.SignedUpUser;
import com.schdlr.model.UserPrincipal;
import com.schdlr.repo.UserManagmentRepo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsConfigService  implements UserDetailsService{

    private final UserManagmentRepo userManagmentRepo;

    /**
     * Constructor for injecting the UserManagmentRepo dependency.
     * userManagmentRepo-Repository for user management operations.
     */
    public UserDetailsConfigService(UserManagmentRepo userManagmentRepo){
        this.userManagmentRepo = userManagmentRepo;
    }


    /*
     * Loads a user by their username. Throws an exception if the user is not found.
     * username-The username to search for.
     * returns UserDetails containing the user's information.
     * throws UsernameNotFoundException If no user with the given username exists.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<SignedUpUser> user = userManagmentRepo.findByUserName(username); // Fetch user from the repository

        // Check if the user is present; otherwise, throw an exception
        if(!user.isPresent()){
            System.out.println("User not found"); // Log message for debugging  
            throw new UsernameNotFoundException(username); // Throw exception
        }

        return new UserPrincipal(user.get()); // Wrap the user in a UserPrincipal object
    }


    public UserDetails loadUserByEmail(String email) {
        Optional<SignedUpUser> user = userManagmentRepo.findByEmail(email); // Fetch user from the repository

        // Check if the user is present; otherwise, throw an exception
        if(!user.isPresent()){
            System.out.println("User not found"); // Log message for debugging  
            throw new UsernameNotFoundException(email); // Throw exception
        }

        return new UserPrincipal(user.get()); // Wrap the user in a UserPrincipal object
    }

}
