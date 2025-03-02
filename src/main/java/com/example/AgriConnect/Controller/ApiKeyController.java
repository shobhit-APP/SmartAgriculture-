package com.example.AgriConnect.Controller;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api")
public class ApiKeyController {

    @Value("${api.key}")
    private String apikey;
    @GetMapping("/get-api-key")
    @ResponseBody
    public String getApikey()
    {
        return apikey;
    }
}
