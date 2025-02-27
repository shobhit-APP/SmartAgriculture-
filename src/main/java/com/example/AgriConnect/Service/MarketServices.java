package com.example.AgriConnect.Service;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.Crop;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Repository.Repo;
import com.example.AgriConnect.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketServices {
    private static final Logger log = LoggerFactory.getLogger(MarketServices.class);
    private final String flaskUrl ="https://pricepredectionmodel.onrender.com/predict";

    @Autowired
    private Repo repository;

    public Map<String, Object> getPrediction(Crop crop) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("state", crop.getState());
        requestMap.put("district", crop.getDistrict());
        requestMap.put("market", crop.getMarket());
        requestMap.put("crop_name", crop.getCropName());
        requestMap.put("arrival_date", crop.getArrivalDate() != null ? crop.getArrivalDate().toString() : null);
        requestMap.put("min_price", crop.getMinPrice());
        requestMap.put("max_price", crop.getMaxPrice());

        Map<String, Object> response;
        try {
            response = restTemplate.postForObject(flaskUrl, requestMap, Map.class);
            if (response != null && response.containsKey("predicted_price_xgb")) {
                crop.setSuggestedPrice((Double) response.get("predicted_price_xgb"));

                // Logging the fields to check values
                log.info("State: {}", crop.getState());
                log.info("District: {}", crop.getDistrict());
                log.info("Market: {}", crop.getMarket());
                log.info("Crop Name: {}", crop.getCropName());
                log.info("Arrival Date: {}", crop.getArrivalDate());
                log.info("Min Price: {}", crop.getMinPrice());
                log.info("Max Price: {}", crop.getMaxPrice());
                log.info("Suggested Price: {}", crop.getSuggestedPrice());

                repository.save(crop);
            }
        } catch (ResourceAccessException e) {
            log.error("Error accessing Flask URL: {}", flaskUrl, e);
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
        return response;
    }

    public List<Crop> GetAllMarketDetailsById(Long UserId) {
       return  repository.findByUserDetails1UserId(UserId);
    }
}