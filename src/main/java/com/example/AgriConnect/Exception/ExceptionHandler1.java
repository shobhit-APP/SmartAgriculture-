package com.example.AgriConnect.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ExceptionHandler1 {

    @ExceptionHandler(AnyException.class)
    public String handleAnyException(AnyException e, Model model) {
        model.addAttribute("status", "Error");
        model.addAttribute("error_message", e.getMessage());
        return "error";
    }
    // ✅ Handle MethodArgumentNotValidException separately
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ModelAndView mav = new ModelAndView("error"); // Load error.html
        mav.addObject("status", "Validation Error");
        mav.addObject("error_message", errors);

        return mav;
    }

    // ✅ Handle other generic exceptions
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", "Internal Server Error");
        mav.addObject("error_message", ex.getMessage());

        return mav;
    }


}
