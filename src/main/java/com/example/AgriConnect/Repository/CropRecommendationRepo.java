package com.example.AgriConnect.Repository;

import com.example.AgriConnect.Model.CropRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import java.util.Set;

import java.util.List;

@Repository
public interface CropRecommendationRepo extends JpaRepository<CropRecommendation, Integer> {
    // Fetch full CropRecommendation entities by user ID
    List<CropRecommendation> findByUserDetails1UserId(Long userId);

    // Fetch distinct PredictedCrop values (Strings) by user ID
    @Query("SELECT DISTINCT cr.PredictedCrop FROM CropRecommendation cr WHERE cr.userDetails1.userId = :userId")
    Set<String> findDistinctPredictedCropsByUserId(Long userId);

    // Fetch CropRecommendation entities filtered by user ID and PredictedCrop
    @Query("SELECT cr FROM CropRecommendation cr WHERE cr.userDetails1.userId = :userId AND cr.PredictedCrop = :crop")
    List<CropRecommendation> findByUserIdAndPredictedCrop(@Param("userId") Long userId, @Param("crop") String crop);
}