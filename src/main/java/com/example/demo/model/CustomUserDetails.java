package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private boolean rememberMe;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
            boolean rememberMe) {
        super(username, password, true, true, true, true, authorities);
        this.rememberMe = rememberMe;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

}
