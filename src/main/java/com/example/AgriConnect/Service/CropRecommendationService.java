package com.example.AgriConnect.Service;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.CropInfo;
import com.example.AgriConnect.Model.CropRecommendation;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Repository.CropRecommendationRepo;
import com.example.AgriConnect.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CropRecommendationService {
    @Autowired
    private TranslateToHindi translateToHindi;
    private  static final Logger log = LoggerFactory.getLogger(CropRecommendationService.class);
    private final String flaskurl="http://159.65.158.161:8080/recommend";
    @Autowired
    private CropRecommendationRepo CropRecommendationRepo;
    @Autowired
    private UserRepo repo;
    public Map<String, Object> GetRecommendation(CropRecommendation cropRecommendation) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("N", cropRecommendation.getN());
        requestMap.put("P", cropRecommendation.getP());
        requestMap.put("K", cropRecommendation.getK());
        requestMap.put("temperature", cropRecommendation.getTemperature());
        requestMap.put("humidity", cropRecommendation.getHumidity());
        requestMap.put("rainfall", cropRecommendation.getRainfall());
        requestMap.put("ph", cropRecommendation.getPh());

        log.info("Request Map: {}", requestMap);

        Map<String, Object> responses;
        try {
            responses = restTemplate.postForObject(flaskurl, requestMap, Map.class);

            if (responses != null && responses.containsKey("predicted_crop")) {
                String predictedCrop = (String) responses.get("predicted_crop");
                cropRecommendation.setPredictedCrop(predictedCrop);

                // Get CropInfo including Hindi name and both descriptions
                CropInfo cropInfo = translateToHindi.getCropInfo(predictedCrop);

                // Set Hindi crop name and descriptions in CropRecommendation entity
                cropRecommendation.setPredictedCropHindi(cropInfo.getHindiName());
                cropRecommendation.setHindiDescription(cropInfo.getHindiDescription());
                cropRecommendation.setEnglishDescription(cropInfo.getEnglishDescription());

                // Add them to response map as well
                responses.put("hindi_name", cropInfo.getHindiName());
                responses.put("hindi_description", cropInfo.getHindiDescription());
                responses.put("english_description", cropInfo.getEnglishDescription());
                responses.put("temperature",requestMap.get("temperature"));
                    String waterNeeds = WaterNeeds(cropRecommendation.getRainfall());
                    responses.put("water_needs", waterNeeds);

                // Save updated recommendation to DB
                CropRecommendationRepo.save(cropRecommendation);

                // Logging for debugging
                log.info("Predicted Crop: {}", predictedCrop);
                log.info("Hindi Name: {}", cropInfo.getHindiName());
                log.info("Hindi Description: {}", cropInfo.getHindiDescription());
                log.info("English Description: {}", cropInfo.getEnglishDescription());
            }
        } catch (ResourceAccessException e) {
            log.error("Error accessing Flask URL: {}", flaskurl, e);
            throw new AnyException("Sorry, we couldn't process your request at the moment. Please try again later");
        } catch (HttpClientErrorException e) {
            log.error("Client error: {}", e.getStatusCode(), e);
            throw new AnyException("Sorry, an error occurred while processing your request");
        } catch (Exception e) {
            log.error("Unexpected error occurred: ", e);
            throw new AnyException("Sorry, an unexpected error occurred. Please try again later.");
        }

        return responses;
    }

    public List<CropRecommendation> GetSavedRecommendCropByUserId(Long UserId) {
        return CropRecommendationRepo.findByUserDetails1UserId(UserId);
    }

    public Set<String> getUniqueCrops(Long UserId) {
        return CropRecommendationRepo.findDistinctPredictedCropsByUserId(UserId);
    }

public List<CropRecommendation> getRecommendations(String Crop,Long UserId) {
         return CropRecommendationRepo.findByUserIdAndPredictedCrop(UserId,Crop);
}
 private String WaterNeeds(float rainfall)
 {
     String waterNeeds;
     if (rainfall < 200) {
         waterNeeds = "High";
     } else if (rainfall >= 200 && rainfall <= 800) {
         waterNeeds = "Medium";
     } else {
         waterNeeds = "Low";
     }
    return waterNeeds;

 }

}
