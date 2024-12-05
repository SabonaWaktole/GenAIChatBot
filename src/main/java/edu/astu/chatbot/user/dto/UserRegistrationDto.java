package edu.astu.chatbot.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotEmpty(message = "Full name is required and must not be empty")
    private String fullName;

    @NotEmpty(message = "Username is required and must not be empty")
    private String username;


    @NotEmpty(message = "Password is required and must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}