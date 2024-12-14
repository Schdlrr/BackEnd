package com.schdlr.model;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;

import com.schdlr.model.SignedUser;

/*
 * UserPrincipal is an implementation of UserDetails used by Spring Security.
 * It encapsulates user details required for authentication and authorization.
 * 
 * Annotations:
 * None (relies on Spring Security interface).
 * 
 * Responsibilities:
 * - Provide user-specific details such as username, password, and authorities.
 * - Indicate account status (non-expired, non-locked, etc.).
 * 
 * Fields:
 * - `user`: The signed-up user whose details are encapsulated.
 */

public class UserPrincipal implements UserDetails{

    private SignedUser user;

    /*
     * Constructor to initialize UserPrincipal with a SignedUpUser.
     * 
     * user - The signed-up user entity.
     */
    public UserPrincipal(SignedUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         // Grants a single authority "USER" to the user
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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
