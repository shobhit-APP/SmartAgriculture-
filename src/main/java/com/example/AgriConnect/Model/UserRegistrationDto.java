package com.example.AgriConnect.Model;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String userPassword;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "Contact number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Phone number should be exactly 10 digits")
    private String contactNumber;
}
