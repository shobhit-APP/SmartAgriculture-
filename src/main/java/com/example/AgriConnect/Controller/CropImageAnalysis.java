package com.example.AgriConnect.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/api")
public class CropImageAnalysis {
    @GetMapping("/ImageAnalysis")
    public String cropImageAnalysis()
    {
        return "ImageAnalysis";
    }
}
