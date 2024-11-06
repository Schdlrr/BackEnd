package com.schdlr.service;


import com.schdlr.model.User;
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
        
        User user = userManagmentRepo.findByUsername(username);

        if(user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }

        return new UserPrincipal(user);
    }

}
