package com.example.AgriConnect.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class ExceptionHandler1 {
    @ExceptionHandler(AnyException.class)
    public String HandelAnyException(AnyException e , Model model)
    {
        model.addAttribute("status", "Error");
        model.addAttribute("error_message", e.getMessage());
        return "error";
    }
}
