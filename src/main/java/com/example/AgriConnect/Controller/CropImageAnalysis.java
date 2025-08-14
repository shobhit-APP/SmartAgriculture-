package com.example.AgriConnect.Controller;

import com.example.AgriConnect.Service.CropImageAnalysisService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/api")
public class CropImageAnalysis {

    @Autowired
    private CropImageAnalysisService cropImageAnalysisService;

    @Autowired
    private MessageSource messageSource;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/ImageAnalysis")
    public String cropImageAnalysis(Model model, Locale locale) {
        model.addAttribute("pageTitle", messageSource.getMessage("imageanalysis.title", null, locale));
        return "ImageAnalysis";
    }

    @PostMapping("/analyzeImage")
    public String analyzeImage(@RequestParam("file") MultipartFile file,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        if (file.isEmpty()) {
            model.addAttribute("error", messageSource.getMessage("imageanalysis.error.no_file", null, locale));
            return "ImageAnalysis";
        }

        try {
            String result = cropImageAnalysisService.analyzeImage(file, locale);

            if (result.startsWith("ERROR:")) {
                model.addAttribute("error", result.substring(6)); // Remove "ERROR:" prefix for localized handling
            } else {
                JsonNode jsonNode = objectMapper.readTree(result);
                if (jsonNode.has("error")) {
                    model.addAttribute("error", messageSource.getMessage("imageanalysis.error.json", new Object[]{jsonNode.get("error").asText()}, locale));
                } else {
                    cropImageAnalysisService.processImageAnalysis(jsonNode, model, locale);
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("imageanalysis.error.exception", new Object[]{e.getMessage()}, locale));
        }

        return "ImageAnalysis"; // Consider redirect if needed
    }

    @GetMapping("/getAnalysisStatus/{filename}")
    @ResponseBody
    public String getAnalysisStatus(@PathVariable String filename, Locale locale) {
        return cropImageAnalysisService.getAnalysisStatus(filename, locale);
    }
}