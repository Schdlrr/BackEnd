package com.schdlr.dto;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;

import com.schdlr.model.BusinessOwner;
import com.schdlr.model.SignedUser;

public class UserPrincipal implements UserDetails{

    private SignedUser user;

    private BusinessOwner businessOwner;

    public UserPrincipal(SignedUser user) {
        this.user = user;
    }

    public UserPrincipal(BusinessOwner businessOwner){
        this.businessOwner = businessOwner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user != null) {
            return Collections.singleton(new SimpleGrantedAuthority("SIGNED_USER"));
        } else if (businessOwner != null) {
            return Collections.singleton(new SimpleGrantedAuthority("BUSINESS_OWNER"));
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        if (user != null) {
            return user.getPassword();
        } else if (businessOwner != null) {
            return businessOwner.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (user != null) {
            return user.getUserName();
        } else if (businessOwner != null) {
            return businessOwner.getUserName();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
