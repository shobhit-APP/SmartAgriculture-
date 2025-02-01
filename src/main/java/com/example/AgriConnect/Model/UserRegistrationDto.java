package com.example.AgriConnect.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String UserPassword;
    private String UserEmail;
    private Long ContactNumber;
}
