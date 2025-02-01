package com.example.AgriConnect.Repository;

import com.example.AgriConnect.Model.CropRecommendation;
import com.example.AgriConnect.Model.UserDetails1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRecommendationRepo extends JpaRepository<CropRecommendation,Integer> {
    List<CropRecommendation> findByUserDetails1UserId(Long userId);
}
