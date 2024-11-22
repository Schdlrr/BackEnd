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

    public UserDetailsConfigService(UserManagmentRepo userManagmentRepo){
        this.userManagmentRepo = userManagmentRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<SignedUpUser> user = userManagmentRepo.findByUserName(username);

        if(!user.isPresent()){
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }

        return new UserPrincipal(user.get());
    }

}
