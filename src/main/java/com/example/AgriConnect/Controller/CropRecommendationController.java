package com.example.AgriConnect.Controller;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.Crop;
import com.example.AgriConnect.Model.CropRecommendation;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Service.CropRecommendationService;
import com.example.AgriConnect.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        model.addAttribute("message","Your Input Submitted Successfully wait for recommendation");
        model.addAttribute("recommendedResult",RecommendationResult);
        return "recommendedResult";
    }
    @GetMapping("/dashboard1/{UserId}")
    public String GetSavedRecommendation(@PathVariable("UserId") Long UserId, Model model) {
       List<CropRecommendation> recommendationList=cropRecommendationService.GetSavedRecommendCropByUserId(UserId);
        if(recommendationList.isEmpty())
        {
            log.error("There is No Previous Crop Price Prediction Result with This UserId {}" ,  UserId);
            throw new AnyException("Sorry!, There is No Previous Crop Price Prediction Result with This UserId : " + UserId);
        }
        model.addAttribute("recommendationList",recommendationList);
        return "dashboard1";
    }
}
