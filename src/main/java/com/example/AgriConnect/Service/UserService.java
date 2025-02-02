
package com.example.AgriConnect.Service;

import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Model.UserRegistrationDto;
import com.example.AgriConnect.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    public UserDetails1 register(UserRegistrationDto registrationDto) {
        UserDetails1 userDetails = new UserDetails1();
        userDetails.setUsername(registrationDto.getUsername());
        userDetails.setUserPassword(passwordEncoder.encode(registrationDto.getUserPassword()));
        userDetails.setUserEmail(registrationDto.getUserEmail());
        userDetails.setContactNumber(registrationDto.getContactNumber());
        userRepo.save(userDetails);
        return userDetails;
    }

    // Update user details
    public UserDetails1 updateUser(Long userId, UserDetails1 userDetails) {
        UserDetails1 existingUser = userRepo.findById(userId).orElse(null);
        if (existingUser == null) {
            return null;
        }

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setUserPassword(passwordEncoder.encode(userDetails.getUserPassword()));
        existingUser.setUserEmail(userDetails.getUserEmail());
        existingUser.setContactNumber(userDetails.getContactNumber());
        return userRepo.save(existingUser);
    }

    public void deleteUser(String username) {
        userRepo.deleteByUsername(username);
    }

    public UserDetails1 findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public UserDetails1 findByUserId(Long sessionId) {
        return  userRepo.findById(sessionId).orElseThrow(()->new RuntimeException("User Not Found"));
    }
}


