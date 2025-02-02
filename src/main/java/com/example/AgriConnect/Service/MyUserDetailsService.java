package com.example.AgriConnect.Service;

import com.example.AgriConnect.Model.UserPrinciples;
import com.example.AgriConnect.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public UserDetails
    loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.AgriConnect.Model.UserDetails1 user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not Found");
        }
        return new UserPrinciples(user);

    }

}