package com.example.AgriConnect.Repository;

import com.example.AgriConnect.Model.UserDetails1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserDetails1,Long> {
    UserDetails1 findByUsername(String Username);
    void deleteByUsername(String username);
    boolean existsByPhoneNumber(String contactNumber);
    boolean existsByemail(String userEmail);
}
