package com.example.AgriConnect.Model;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class UserPrinciples implements UserDetails {
    private com.example.AgriConnect.Model.UserDetails1 User;
    public UserPrinciples(com.example.AgriConnect.Model.UserDetails1 User)
    {
        this.User=User;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("User"));
    }
    @Override
    public String getPassword() {
        return User.getUserPassword();
    }
    @Override
    public String getUsername() {
       return User.getUsername();
    }
    public String getUserEmail()
    {
        return User.getUserEmail();
    }
    public Long getContactNumber()
    {
        return User.getContactNumber();
    }
    public Long getUserId()
    {
        return User.getUserId();
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
