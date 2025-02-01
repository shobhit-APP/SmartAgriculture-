package com.example.AgriConnect.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            switch (statusCode) {
                case 500:
                    model.addAttribute("status", "Error 500");
                    model.addAttribute("message", "Internal Server Error");
                    break;
                case 404:
                    model.addAttribute("status", "Error 404");
                    model.addAttribute("message", "This Page Not Found");
                    break;
                case 502:
                    model.addAttribute("status", "Error 502");
                    model.addAttribute("message", "Bad Request Gateway");
                    break;
                case 405:
                    model.addAttribute("status", "Error 405");
                    model.addAttribute("message", "Method Not Allowed");
                    break;
                case 401:
                    model.addAttribute("status", "Error 401");
                    model.addAttribute("message", "Unauthorized Access");
                    break;
                case 403:
                    model.addAttribute("status", "Error 403");
                    model.addAttribute("message", "Forbidden");
                    break;
                case 409:
                    model.addAttribute("status", "Error 409");
                    model.addAttribute("message", "Conflict");
                    break;
                case 410:
                    model.addAttribute("status", "Error 410");
                    model.addAttribute("message", "Gone");
                    break;
                case 415:
                    model.addAttribute("status", "Error 415");
                    model.addAttribute("message", "Unsupported Media Type");
                    break;
                case 429:
                    model.addAttribute("status", "Error 429");
                    model.addAttribute("message", "Too Many Requests");
                    break;
                case 503:
                    model.addAttribute("status", "Error 503");
                    model.addAttribute("message", "Service Unavailable");
                    break;
                default:
                    model.addAttribute("status", "Error");
                    model.addAttribute("message", "An unexpected error occurred.");
            }
        }
        return "error";
    }
}
