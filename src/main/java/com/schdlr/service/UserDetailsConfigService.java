package com.schdlr.service;


import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

import com.schdlr.dto.UserPrincipal;
import com.schdlr.model.SignedUser;
import com.schdlr.repo.BusinessOwnerRepo;
import com.schdlr.repo.UserManagmentRepo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsConfigService  implements UserDetailsService{

    private UserManagmentRepo userManagmentRepo;
    private BusinessOwnerRepo businessOwnerRepo;

    public UserDetailsConfigService(UserManagmentRepo userManagmentRepo, BusinessOwnerRepo businessOwnerRepo){
        this.userManagmentRepo = userManagmentRepo;
        this.businessOwnerRepo = businessOwnerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<SignedUser> user = userManagmentRepo.findByUserName(username);

        if(!user.isPresent()){
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }

        return new UserPrincipal(user.get());
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
