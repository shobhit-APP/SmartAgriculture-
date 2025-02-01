package com.example.AgriConnect.Service;

import com.example.AgriConnect.Exception.AnyException;
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

@Service
public class CropRecommendationService {
    private  static final Logger log = LoggerFactory.getLogger(CropRecommendationService.class);
    private final String flaskurl="https://agripredict-hfgk.onrender.com/recommend";
    @Autowired
    private CropRecommendationRepo CropRecommendationRepo;
    @Autowired
    private UserRepo repo;
    public Map<String ,Object> GetRecommendation(CropRecommendation cropRecommendation)
    {

        RestTemplate restTemplate =new RestTemplate();
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("N",cropRecommendation.getN());
        requestMap.put("P",cropRecommendation.getP());
        requestMap.put("K",cropRecommendation.getK());
        requestMap.put("temperature",cropRecommendation.getTemperature());
        requestMap.put("humidity",cropRecommendation.getHumidity());
        requestMap.put("rainfall",cropRecommendation.getRainfall());
        requestMap.put("ph",cropRecommendation.getPh());
        log.info("Request Map: {}", requestMap);
        Map<String,Object> responses;
        try
        {
             responses=restTemplate.postForObject(flaskurl,requestMap,Map.class);
             if(responses!=null&&responses.containsKey("predicted_crop"))
            {
                cropRecommendation.setPredictedCrop((String) responses.get("predicted_crop"));
                log.info("Potassium {}", cropRecommendation.getK());
                log.info("Nitrogen {}", cropRecommendation.getN());
                log.info("Phosphorus {}", cropRecommendation.getP());
                log.info("Temperature {}", cropRecommendation.getTemperature());
                log.info("Humidity {}", cropRecommendation.getHumidity());
                log.info("Rainfall {}", cropRecommendation.getRainfall());
                log.info("PH {}", cropRecommendation.getPh());
                CropRecommendationRepo.save(cropRecommendation);
            }
        } catch (ResourceAccessException e) {
            log.error("Error accessing Flask URL: {}", flaskurl, e);
             throw new AnyException("Sorry, we couldn't process your request at the moment. Please try again later");
           }
        catch (HttpClientErrorException e) {
            log.error("Client error: {}", e.getStatusCode(), e);
            throw new AnyException("Sorry, an error occurred while processing your request");
        }
        catch (Exception e) {
            log.error("Unexpected error occurred: ", e);
            throw new AnyException("Sorry, an unexpected error occurred. Please try again later.");
        }
        return responses;
    }
    public List<CropRecommendation> GetSavedRecommendCropByUserId(Long UserId) {
        return CropRecommendationRepo.findByUserDetails1UserId(UserId);
    }
}
