package com.example.AgriConnect.Controller;

import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Model.UserPrinciples;
import com.example.AgriConnect.Model.UserRegistrationDto;
import com.example.AgriConnect.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class UserController {
    public static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource; // Inject MessageSource for localization

    @GetMapping("/Agriconnect.com")
    public String homepage(Model model, Locale locale) {
        return "Agriconnect";
    }

    @GetMapping("/Agriconnect")
    public String homepage1(Model model, Locale locale) {
        return "Agriconnect";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Locale locale) {
        return "register";
    }

    @GetMapping("/terms")
    public String terms(Model model, Locale locale) {
        return "terms";
    }

    @GetMapping("/login")
    public String loginPage(Model model, Locale locale) {
        return "login";
    }

    @GetMapping("/Option")
    public String showOptionsPage(Model model, HttpSession session,
            @AuthenticationPrincipal UserPrinciples userPrinciples, Locale locale) {
        String username = userPrinciples.getUsername();
        if (username == null) {
            log.error(messageSource.getMessage("option.error.username_not_found", null, locale));
            String errorMessage = messageSource.getMessage("option.error.username_not_found", null, locale);
            throw new AnyException(errorMessage);
        }

        try {
            UserDetails1 userDetails1 = userService.findByUsername(username);
            if (userDetails1 == null) {
                log.error(
                        messageSource.getMessage("option.error.user_not_found_log", new Object[] { username }, locale));
                String errorMessage = messageSource.getMessage("option.error.user_not_found", new Object[] { username },
                        locale);
                throw new AnyException(errorMessage);
            }
            session.setAttribute("UserId", userDetails1.getUserId());
        } catch (Exception e) {
            log.error(messageSource.getMessage("option.error.session_setup_log",
                    new Object[] { username, e.getMessage() }, locale));
            String errorMessage = messageSource.getMessage("option.error.session_setup", null, locale);
            throw new AnyException(errorMessage);
        }

        Long userId = (Long) session.getAttribute("UserId");
        log.info("UserId:{}", userId);
        model.addAttribute("pageTitle", messageSource.getMessage("option.title", null, locale));
        model.addAttribute("UserId", userId); // Add UserId to model for dynamic links
        return "Option"; // Thymeleaf template for options page
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @ModelAttribute UserRegistrationDto registrationDto, BindingResult result,
            Locale locale) {
        if (result.hasErrors()) {
            // Collect validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            // Return JSON response with 400 Bad Request status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", messageSource.getMessage("register.response.validation_error", null, locale),
                            "errors", errors));
        }

        try {
            userService.register(registrationDto);
            System.out.println(messageSource.getMessage("register.log.password_received", null, locale));
            return ResponseEntity.ok(Map.of(
                    "status", messageSource.getMessage("register.response.success", null, locale),
                    "message", messageSource.getMessage("register.response.success_message", null, locale)));
        } catch (Exception e) {
            // Return JSON response with 500 Internal Server Error status
            log.error(messageSource.getMessage("register.log.error_registering", new Object[] { e.getMessage() },
                    locale));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", messageSource.getMessage("register.response.error", null, locale),
                            "message", messageSource.getMessage("register.response.error_message", null, locale),
                            "details", e.getMessage()));
        }
    }

    // Update user details
    @PutMapping("/UpdateUser/{UserId}")
    public ResponseEntity<String> updateUser(@PathVariable Long UserId, @RequestBody UserDetails1 userDetails,
            Locale locale) {
        UserDetails1 updatedUserDetails = userService.updateUser(UserId, userDetails);
        if (updatedUserDetails == null) {
            return new ResponseEntity<>(
                    messageSource.getMessage("updateuser.response.failure", null, locale),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                messageSource.getMessage("updateuser.response.success", null, locale),
                HttpStatus.OK);
    }

    @GetMapping("/logout-success")
    public String logout(HttpServletRequest request, Model model, Locale locale) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("pageTitle", messageSource.getMessage("logout.title", null, locale));
        model.addAttribute("message", messageSource.getMessage("logout.message", null, locale));
        model.addAttribute("secondsLeft", 20);
        request.getCookies();
        return "logout";
    }

    @GetMapping("/ChatBot")
    public String chatBot(Model model, Locale locale) {
        model.addAttribute("pageTitle", messageSource.getMessage("chatbot.header.title", null, locale));
        return "ChatBot";
    }
}