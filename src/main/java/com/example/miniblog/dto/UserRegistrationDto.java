package com.example.miniblog.dto;

import jakarta.validation.constraints.*;

public class UserRegistrationDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    @Email
    private String email;

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }

    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }

    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
}
