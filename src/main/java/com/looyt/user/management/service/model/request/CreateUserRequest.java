package com.looyt.user.management.service.model.request;

import com.looyt.user.management.service.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank(message = "User name is required")
    @Size(min = 2, max = 50, message = "User name must be between 2 and 50 characters")
    private String username;

    @Email()
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private UserRole role;
}
