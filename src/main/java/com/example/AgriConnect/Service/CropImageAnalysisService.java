package com.example.AgriConnect.Service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CropImageAnalysisService {

    @Value("${flask.api.url:http://localhost:5000}")
    private String flaskApiUrl;

    @Value("${image.upload.dir:./uploads}")
    private String uploadDir;
    @Autowired
    private  MessageSource messageSource;

    private final RestTemplate restTemplate;
    private final List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "jfif", "heic");

    public CropImageAnalysisService() {
        this.restTemplate = new RestTemplate();
    }

    public String analyzeImage(MultipartFile file, Locale locale) throws IOException {
        // Validate file
        if (!isValidImageFile(file)) {
            return "ERROR:अमान्य फाइल प्रकार। केवल JPG, PNG, JPEG फाइलें अपलोड करें / Invalid file type. Only JPG, PNG, JPEG files allowed.";
        }

        // Create upload directory if it doesn't exist
        createUploadDirectory();

        // Save file locally first
        String filename = generateUniqueFilename(file.getOriginalFilename());
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        try {
            // Send file to Flask API
            return sendToFlaskAPI(filePath.toFile(), locale);

        } catch (Exception e) {
            // Clean up file if API call fails
            Files.deleteIfExists(filePath);
            return messageSource.getMessage("imageanalysis.error.exception", new Object[]{e.getMessage()}, locale);
        }
    }

    private boolean isValidImageFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            return false;
        }
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        return allowedExtensions.contains(extension);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    private void createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extension;
    }

    private String sendToFlaskAPI(File imageFile, Locale locale) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(imageFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    flaskApiUrl + "/analysis",
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return messageSource.getMessage("imageanalysis.error.api_failure", null, locale);
            }

        } catch (Exception e) {
            return messageSource.getMessage("imageanalysis.error.api_failure", new Object[]{e.getMessage()}, locale);
        }
    }

    public String getAnalysisStatus(String filename, Locale locale) {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        return Files.exists(filePath) ? "completed" : messageSource.getMessage("imageanalysis.error.no_file", null, locale);
    }

    public void cleanupOldFiles(int daysOld) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (Files.exists(uploadPath)) {
                Files.list(uploadPath)
                        .filter(Files::isRegularFile)
                        .filter(file -> {
                            try {
                                return Files.getLastModifiedTime(file).toMillis() <
                                        System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L);
                            } catch (IOException e) {
                                return false;
                            }
                        })
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                // Log error but continue
                            }
                        });
            }
        } catch (IOException e) {
            // Log error
        }
    }

    public void processImageAnalysis(JsonNode jsonNode, Model model, Locale locale) {
        try {
            JsonNode info = jsonNode.get("info");
            if (info == null) {
                model.addAttribute("error", messageSource.getMessage("imageanalysis.error.generic", null, locale));
                return;
            }

            String causeEn = info.has("cause_en") ? info.get("cause_en").asText() : "Unknown";
            String causeHi = info.has("cause_hi") ? info.get("cause_hi").asText() : "अज्ञात";
            String nameEn = info.has("name_en") ? info.get("name_en").asText() : "Unknown";
            String nameHi = info.has("name_hi") ? info.get("name_hi").asText() : "अज्ञात";
            String suggestionEn = info.has("suggestion_en") ? info.get("suggestion_en").asText() : "No suggestion available";
            String suggestionHi = info.has("suggestion_hi") ? info.get("suggestion_hi").asText() : "कोई सुझाव उपलब्ध नहीं";
            String symptomsEn = info.has("symptoms_en") ? info.get("symptoms_en").asText() : "No symptoms detected";
            String symptomsHi = info.has("symptoms_hi") ? info.get("symptoms_hi").asText() : "कोई लक्षण नहीं पाया गया";

            model.addAttribute("analysisResultEn", String.format(
                    "<div class='result-item'><span class='result-label'><i class='fas fa-virus'></i> Disease:</span><span class='result-value'>%s</span></div>" +
                            "<div class='result-item'><span class='result-label'><i class='fas fa-search'></i> Cause:</span><span class='result-value'>%s</span></div>" +
                            "<div class='result-item'><span class='result-label'><i class='fas fa-eye'></i> Symptoms:</span><span class='result-value'>%s</span></div>" +
                            "<div class='result-item'><span class='result-label'><i class='fas fa-lightbulb'></i> Suggestion:</span><span class='result-value'>%s</span></div>",
                    escapeHtml(nameEn), escapeHtml(causeEn), escapeHtml(symptomsEn), escapeHtml(suggestionEn)));

            model.addAttribute("analysisResultHi", String.format(
                    "<div class='result-item'><span class='result-label'><i class='fas fa-virus'></i> रोग:</span><span class='result-value'>%s</span></div>" +
                            "<div class='result-item'><span class='result-label'><i class='fas fa-search'></i> कारण:</span><span class='result-value'>%s</span></div>" +
                            "<div class='result-item'><span class='result-label'><i class='fas fa-eye'></i> लक्षण:</span><span class='result-value'>%s</span></div>" +
                            "<div class='result-item'><span class='result-label'><i class='fas fa-lightbulb'></i> सुझाव:</span><span class='result-value'>%s</span></div>",
                    escapeHtml(nameHi), escapeHtml(causeHi), escapeHtml(symptomsHi), escapeHtml(suggestionHi)));

            model.addAttribute("success", true);
            model.addAttribute("showResults", true);
        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("imageanalysis.error.exception", new Object[]{e.getMessage()}, locale));
            model.addAttribute("success", false);
            model.addAttribute("showResults", false);
        }
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}