package com.schdlr.service;


import com.schdlr.dto.UserPrincipal;
import com.schdlr.model.SignedUser;
import com.schdlr.repo.BusinessOwnerRepo;
import com.schdlr.repo.SignedUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Slf4j
@Service
public class UserDetailsConfigService  implements UserDetailsService{

    private SignedUserRepo signedUserRepo;
    private BusinessOwnerRepo businessOwnerRepo;

    public UserDetailsConfigService(SignedUserRepo signedUserRepo, BusinessOwnerRepo businessOwnerRepo){
        this.signedUserRepo = signedUserRepo;
        this.businessOwnerRepo = businessOwnerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<SignedUser> user = signedUserRepo.findByUserName(username);

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
            return signedUserRepo.findByEmail(email)
                    .map(UserPrincipal::new)
                    .orElseThrow(() -> {
                        log.info("User is not a signed user");
                        return new UserPrincipalNotFoundException(email);
                    });
        }
    
        throw new IllegalArgumentException("Invalid role provided: " + role);
    }

}
