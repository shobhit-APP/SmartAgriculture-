
package com.example.AgriConnect.Service;

import com.example.AgriConnect.Exception.AnyException;
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

    public UserDetails1 register(UserRegistrationDto registrationDto) {
        if (registrationDto == null) {
            throw new AnyException("Registration details cannot be null.");
        }

        // Validate email existence
        String email = registrationDto.getUserEmail();
        if (email != null && userRepo.existsByUserEmail(email)) {
            throw new AnyException("Email is already registered.");
        }

        // Validate phone number existence
        String phoneNumber = registrationDto.getContactNumber();
        if (phoneNumber != null && userRepo.existsByContactNumber(phoneNumber)) {
            throw new AnyException("Phone number is already registered.");
        }

        // Create and populate UserDetails1 entity
        UserDetails1 userDetails = new UserDetails1();
        userDetails.setUsername(registrationDto.getUsername());
        userDetails.setUserPassword(passwordEncoder.encode(registrationDto.getUserPassword()));
        userDetails.setUserEmail(email);
        userDetails.setContactNumber(phoneNumber);

        // Save user details in the database
        return userRepo.save(userDetails);
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


