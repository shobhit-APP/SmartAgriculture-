package com.example.AgriConnect.Controller;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.Crop;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Model.UserPrinciples;
import com.example.AgriConnect.Service.MarketServices;
import com.example.AgriConnect.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/* This Annotation Is Used To Create RestFull Service An Handles All
   The Request From Client Side To Gain Various Services
   It Is Combination Of Controller And ResponseBody Annotation
   So You Don't Need To Use ResponseBody To Mapped ResponseBody
   To Get Any Services....
 */
@Controller
/*
    The RequestMapping Annotation Is Used To Map The Request
    For any Specific Body
    Let's suppose You Have  Request of any Method Then You Should Use
    That Annotations To mapped That Service of Method
 */
@RequestMapping("/api")
public class CropPriceController {
    public static final Logger log= LoggerFactory.getLogger(CropPriceController.class);
    /*
        This is Filed Injection To Inject The Object  of Any Component
        Here is Crop Is The Crop Component
        Autowired  Annotation Is The Annotation Is Used To Connect with of Crop
        or To Initialize The Crop....
     */
    @Autowired
    private MarketServices Services;
    @Autowired
    private UserService userService;
    /*
    The GetMapping Annotation Is Used To Map The Request
    For any Specific Body
    Let's suppose You Have  Request to Get marketDetails Then You Should Use
    That Annotations To mapped That Service of That Method
    */
    @GetMapping("/csrf_token")
    public CsrfToken getCsrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute("_csrf");
    }
    @GetMapping("/dashboard")
    public String getAllMarketDetails(
            @AuthenticationPrincipal UserPrinciples userPrinciples,
            Model model,
            @RequestParam(required = false) String state
    ) {
        Long userId = userPrinciples.getUserId();
        List<Crop> marketData = Services.GetAllMarketDetailsById(userId);

        // Handle the case when state is null or empty
        List<Crop> marketData1 = (state != null && !state.isEmpty())
                ? Services.findByStateAndUserId(state, userId)
                : Collections.emptyList();

        if (marketData.isEmpty() && marketData1.isEmpty()) {
            log.error("No Previous Crop Price Prediction Result found for UserId: {}", userId);
            throw new AnyException("Sorry! No Previous Crop Price Prediction Result found for UserId: " + userId);
        }

        // Use different attribute names to avoid overriding
        model.addAttribute("allMarketData", marketData);
        model.addAttribute("stateMarketData", marketData1);
        return "dashboard";
    }

    @GetMapping("/predict")
    public String PricePredictionModel()
    {
        return "predict";
    }
    @PostMapping("/predict")
    public String predict(@ModelAttribute  Crop crop, Model model , HttpSession session)
    {

        log.info("Received Crop Data:");
        log.info("State: {}", crop.getState());
        log.info("District: {}", crop.getDistrict());
        log.info("Market: {}", crop.getMarket());
        log.info("Crop Name: {}", crop.getCropName());
        log.info("Arrival Date: {}", crop.getArrivalDate());
        log.info("Min Price: {}", crop.getMinPrice());
        log.info("Max Price: {}", crop.getMaxPrice());
        Long sessionId=(Long) session.getAttribute("UserId");
        if(sessionId==null)
        {
                log.error("User ID not found in the session. Possible session timeout or invalid session or SessionID Is Not set.");
                throw new AnyException("Sorry!, User session has expired or is invalid. Please log in again.");
        }
        try{
            UserDetails1 userDetails1=userService.findByUserId(sessionId);
            if(userDetails1==null)
            {

                    log.error("User details not found for UserId {}. Possible database issue or incorrect UserId.", sessionId);
                    throw new AnyException("Sorry, we were unable to find your user details. Please verify your UserId or try again later.");

            }
            crop.setUserDetails1(userDetails1);
        }
        catch (Exception e)
        {

            log.error("An unexpected error occurred while finding UserDetails for UserId: {}. Exception: {}", sessionId, e.getMessage());
            throw new AnyException("Sorry, An unexpected error occurred while processing your request. Please try again later. If the problem persists, contact support");

        }

        Map<String,Object> PredictionResult=Services.getPrediction(crop);
        model.addAttribute("message","Your Input Submitted Successfully wait for Prediction");
        model.addAttribute("predictionResult",PredictionResult);
        return "predictionResult";

    }
}
