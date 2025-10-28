package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotEmpty;

public class ResetPasswordRequestDTO {

    @NotEmpty
    private String token;

    @NotEmpty
    private String newPassword;

    @NotEmpty
    private String confirmPassword;

    // Getters e Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}