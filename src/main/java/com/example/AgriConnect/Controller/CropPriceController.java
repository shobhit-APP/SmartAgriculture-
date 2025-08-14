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
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
    public static final Logger log = LoggerFactory.getLogger(CropPriceController.class);

    @Autowired
    private MarketServices services;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource; // Inject MessageSource for localization

    /*
        The GetMapping Annotation Is Used To Map The Request
        For any Specific Body
        Let's suppose You Have  Request to Get marketDetails Then You Should Use
        That Annotations To mapped That Service of That Method
     */
    @GetMapping("/csrf_token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/dashboard")
    public String getAllMarketDetails(@AuthenticationPrincipal UserPrinciples userPrinciples, Model model,
                                      @RequestParam(required = false) String state, Locale locale) {
        Long userId = userPrinciples.getUserId();
        List<Crop> marketData = services.GetAllMarketDetailsById(userId);
        Set<String> uniqueState = services.getUniqueState(userId);

        // Handle the case when state is null or empty
        List<Crop> marketData1 = (state != null && !state.isEmpty())
                ? services.findByStateAndUserId(state, userId)
                : marketData;

        if (marketData.isEmpty() && marketData1.isEmpty()) {
            log.error("No Previous Crop Price Prediction Result found for UserId: {}", userId);
            String errorMessage = messageSource.getMessage("dashboard.error.no_data", new Object[]{userId}, locale);
            throw new AnyException(errorMessage);
        }

        // Add localized attributes
        model.addAttribute("pageTitle", messageSource.getMessage("dashboard.title", null, locale));
        model.addAttribute("state", uniqueState);
        model.addAttribute("allMarketData", marketData);
        return "dashboard";
    }

    @GetMapping("/predict")
    public String pricePredictionModel(Model model, Locale locale) {
        model.addAttribute("pageTitle", messageSource.getMessage("predict.title", null, locale));
        return "predict";
    }

    @PostMapping("/predict")
    public String predict(@ModelAttribute Crop crop, Model model, HttpSession session, Locale locale) {
        log.info("Received Crop Data:");
        log.info("State: {}", crop.getState());
        log.info("District: {}", crop.getDistrict());
        log.info("Market: {}", crop.getMarket());
        log.info("Crop Name: {}", crop.getCropName());
        log.info("Arrival Date: {}", crop.getArrivalDate());
        log.info("Min Price: {}", crop.getMinPrice());
        log.info("Max Price: {}", crop.getMaxPrice());

        Long sessionId = (Long) session.getAttribute("UserId");
        if (sessionId == null) {
            log.error("User ID not found in the session. Possible session timeout or invalid session or SessionID Is Not set.");
            String errorMessage = messageSource.getMessage("predict.error.session_expired", null, locale);
            throw new AnyException(errorMessage);
        }

        try {
            UserDetails1 userDetails1 = userService.findByUserId(sessionId);
            if (userDetails1 == null) {
                log.error("User details not found for UserId {}. Possible database issue or incorrect UserId.", sessionId);
                String errorMessage = messageSource.getMessage("predict.error.user_not_found", new Object[]{sessionId}, locale);
                throw new AnyException(errorMessage);
            }
            crop.setUserDetails1(userDetails1);
        } catch (Exception e) {
            log.error("An unexpected error occurred while finding UserDetails for UserId: {}. Exception: {}", sessionId, e.getMessage());
            String errorMessage = messageSource.getMessage("predict.error.unexpected", null, locale);
            throw new AnyException(errorMessage);
        }

        Map<String, Object> predictionResult = services.getPrediction(crop);
        model.addAttribute("message", messageSource.getMessage("predict.success.message", null, locale));
        model.addAttribute("predictionResult", predictionResult);
        return "predictionResult";
    }
}