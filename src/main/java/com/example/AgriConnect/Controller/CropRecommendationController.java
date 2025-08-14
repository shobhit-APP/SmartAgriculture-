package com.example.AgriConnect.Controller;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.CropRecommendation;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Model.UserPrinciples;
import com.example.AgriConnect.Service.CropRecommendationService;
import com.example.AgriConnect.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api")
public class CropRecommendationController {
    @Autowired
    private CropRecommendationService cropRecommendationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource; // Inject MessageSource for localization

    private static final Logger log = LoggerFactory.getLogger(CropRecommendationController.class);
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH; // Fallback locale for missing translations

    // Helper method to get localized message with fallback
    private String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("Missing message key: {} for locale: {}. Falling back to default locale: {}", code, locale, DEFAULT_LOCALE);
            try {
                return messageSource.getMessage(code, args, DEFAULT_LOCALE);
            } catch (NoSuchMessageException ex) {
                log.error("Message key: {} not found in default locale: {}. Returning default message.", code, DEFAULT_LOCALE);
                return "Message not found: " + code; // Ultimate fallback
            }
        }
    }

    @GetMapping("/recommend")
    public String getRecommendationPage(Model model, Locale locale) {
        model.addAttribute("pageTitle", getMessage("recommend.title", null, locale));
        return "recommend";
    }

    @PostMapping("/recommend")
    public String getRecommendation(@ModelAttribute CropRecommendation cropRecommendation, Model model, HttpSession session, Locale locale) {
        // Log received crop parameters
        log.info("Received Crop Parameters - Nitrogen: {}, Phosphorus: {}, Potassium: {}, Temperature: {}, Humidity: {}, Rainfall: {}, pH: {}",
                cropRecommendation.getN(), cropRecommendation.getP(), cropRecommendation.getK(),
                cropRecommendation.getTemperature(), cropRecommendation.getHumidity(), cropRecommendation.getRainfall(), cropRecommendation.getPh());

        Long sessionId = (Long) session.getAttribute("UserId");
        log.info("UserId from session: {}", sessionId);

        if (sessionId == null) {
            log.error("User ID not found in session. Possible session timeout or invalid session.");
            String errorMessage = getMessage("error.session_expired", null, locale);
            throw new AnyException(errorMessage);
        }

        try {
            UserDetails1 userDetails1 = userService.findByUserId(sessionId);
            if (userDetails1 == null) {
                log.error("User details not found for UserId: {}. Possible database issue.", sessionId);
                String errorMessage = getMessage("error.user_not_found", new Object[]{sessionId}, locale);
                throw new AnyException(errorMessage);
            }
            cropRecommendation.setUserDetails1(userDetails1);
        } catch (Exception e) {
            log.error("Unexpected error finding UserDetails for UserId: {}. Exception: {}", sessionId, e.getMessage());
            String errorMessage = getMessage("error.unexpected", null, locale);
            throw new AnyException(errorMessage);
        }

        try {
            Map<String, Object> recommendationResult = cropRecommendationService.GetRecommendation(cropRecommendation);
            model.addAttribute("message", getMessage("recommend.success.message", null, locale));
            model.addAttribute("recommendedResult", recommendationResult);
            return "recommendedResult";
        } catch (Exception e) {
            log.error("Error processing crop recommendation for UserId: {}. Exception: {}", sessionId, e.getMessage());
            String errorMessage = getMessage("error.unexpected", null, locale);
            throw new AnyException(errorMessage);
        }
    }

    @GetMapping("/dashboard1")
    public String getSavedRecommendation(
            @RequestParam(required = false) String crop,
            @AuthenticationPrincipal UserPrinciples userPrinciples,
            Model model,
            Locale locale) {
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
                String errorMessage = getMessage("recommendation_dashboard.error.no_crop_data", new Object[]{crop, userId}, locale);
                throw new AnyException(errorMessage);
            } else {
                log.error("No previous crop recommendations for user ID: {}", userId);
                String errorMessage = getMessage("recommendation_dashboard.error.no_data", new Object[]{userId}, locale);
                throw new AnyException(errorMessage);
            }
        }

        model.addAttribute("pageTitle", getMessage("recommendation_dashboard.title", null, locale));
        model.addAttribute("recommendationList", displayedRecommendations);
        model.addAttribute("uniqueCrops", uniqueCrops);
        return "dashboard1";
    }
}