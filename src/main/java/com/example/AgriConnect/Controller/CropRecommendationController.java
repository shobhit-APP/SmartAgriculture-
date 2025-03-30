package com.example.AgriConnect.Controller;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.Crop;
import com.example.AgriConnect.Model.CropRecommendation;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Model.UserPrinciples;
import com.example.AgriConnect.Service.CropRecommendationService;
import com.example.AgriConnect.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api")
public class CropRecommendationController {
    @Autowired
    private CropRecommendationService cropRecommendationService;
    @Autowired
    private UserService userService;
    public static final Logger log=LoggerFactory.getLogger(CropRecommendationController.class);
    @GetMapping("/recommend")
    public String GetRecommendationPage()
    {
        return "recommend";
    }
    @PostMapping("/recommend")
    public String GetRecommendation(@ModelAttribute CropRecommendation cropRecommendation , Model model ,HttpSession session )
    {
        //Log received crop Parameters
        log.info("Received Crop Parameter");
        log.info("Nitrogen {}", cropRecommendation.getN());
        log.info("Phosphorus {}", cropRecommendation.getP());
        log.info("Potassium {}", cropRecommendation.getK());
        log.info("Temperature {}", cropRecommendation.getTemperature());
        log.info("Humidity {}", cropRecommendation.getHumidity());
        log.info("Rainfall {}", cropRecommendation.getRainfall());
        log.info("PH {}", cropRecommendation.getPh());
        Long sessionId=(Long)session.getAttribute("UserId");
        log.info("UserId from session: {}" ,sessionId);
        if(sessionId==null)
        {
            log.error("User ID not found in the session. Possible session timeout or invalid session or SessionID Is Not set.");
            throw new AnyException("Sorry!, User session has expired or is invalid. Please log in again.");
        }
            try {
                UserDetails1 userDetails1=userService.findByUserId(sessionId);
                if(userDetails1==null)
                {
                    log.error("User details not found for UserId {}. Possible database issue or incorrect UserId.", sessionId);
                    throw new AnyException("Sorry, we were unable to find your user details. Please verify your UserId or try again later.");
                }
                cropRecommendation.setUserDetails1(userDetails1);
            }
            catch (Exception e)
            {
                log.error("An unexpected error occurred while finding UserDetails for UserId: {}. Exception: {}", sessionId, e.getMessage());
                throw new AnyException("Sorry, An unexpected error occurred while processing your request. Please try again later. If the problem persists, contact support");
            }
        Map<String,Object> RecommendationResult=cropRecommendationService.GetRecommendation(cropRecommendation);
        model.addAttribute("message", "Recommendation complete! Here's the best crop match for your soil conditions.");
        model.addAttribute("recommendedResult",RecommendationResult);
        return "recommendedResult";
    }
    @GetMapping("/dashboard1")
    public String getSavedRecommendation(
            @RequestParam(required = false) String crop,
            @AuthenticationPrincipal UserPrinciples userPrinciples,
            Model model) {
        Long userId = userPrinciples.getUserId();
        List<CropRecommendation> recommendationList = cropRecommendationService.GetSavedRecommendCropByUserId(userId);
        Set<String> uniqueCrops = cropRecommendationService.getUniqueCrops(userId);
        List<CropRecommendation> displayedRecommendations;

        // Handle the case when crop is null or empty
        displayedRecommendations = (crop != null && !crop.isEmpty())
                ? cropRecommendationService.getRecommendations(crop, userId)
                : recommendationList;

        if (displayedRecommendations.isEmpty()) {
            if (crop != null && !crop.isEmpty()) {
                log.warn("No recommendations found for crop: {} for user ID: {}", crop, userId);
                throw new AnyException("Sorry! No Previous Crop Price Prediction Result found for UserId: " + userId);
            } else {
                log.error("No previous crop prediction results for user ID: {}", userId);
                throw new AnyException("Sorry! No previous crop prediction results found for your user ID: " + userId);
            }
        }


        model.addAttribute("recommendationList", displayedRecommendations);
        model.addAttribute("uniqueCrops", uniqueCrops);
        return "dashboard1";
    }
}
