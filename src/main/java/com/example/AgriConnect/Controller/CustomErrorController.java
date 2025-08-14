package com.example.AgriConnect.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/api")
public class CustomErrorController {

    @Autowired
    private MessageSource messageSource; // Inject MessageSource for localization

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Locale locale) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            switch (statusCode) {
                case 500:
                    model.addAttribute("status", messageSource.getMessage("error.status.500", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.500", null, locale));
                    break;
                case 404:
                    model.addAttribute("status", messageSource.getMessage("error.status.404", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.404", null, locale));
                    break;
                case 502:
                    model.addAttribute("status", messageSource.getMessage("error.status.502", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.502", null, locale));
                    break;
                case 405:
                    model.addAttribute("status", messageSource.getMessage("error.status.405", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.405", null, locale));
                    break;
                case 401:
                    model.addAttribute("status", messageSource.getMessage("error.status.401", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.401", null, locale));
                    break;
                case 403:
                    model.addAttribute("status", messageSource.getMessage("error.status.403", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.403", null, locale));
                    break;
                case 409:
                    model.addAttribute("status", messageSource.getMessage("error.status.409", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.409", null, locale));
                    break;
                case 410:
                    model.addAttribute("status", messageSource.getMessage("error.status.410", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.410", null, locale));
                    break;
                case 415:
                    model.addAttribute("status", messageSource.getMessage("error.status.415", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.415", null, locale));
                    break;
                case 429:
                    model.addAttribute("status", messageSource.getMessage("error.status.429", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.429", null, locale));
                    break;
                case 503:
                    model.addAttribute("status", messageSource.getMessage("error.status.503", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.503", null, locale));
                    break;
                case 400:
                    model.addAttribute("status", messageSource.getMessage("error.status.400", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.400", null, locale));
                    break;
                default:
                    model.addAttribute("status", messageSource.getMessage("error.status.default", null, locale));
                    model.addAttribute("message", messageSource.getMessage("error.message.default", null, locale));
            }
        }
        return "error";
    }
}