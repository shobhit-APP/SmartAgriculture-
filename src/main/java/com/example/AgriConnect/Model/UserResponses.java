package com.example.AgriConnect.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponses {
    private String username;
    private String UserEmail;
    private Long ContactNumber;
}
