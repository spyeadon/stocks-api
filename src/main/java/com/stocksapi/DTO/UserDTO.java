package com.stocksapi.DTO;

import com.stocksapi.Validation.ValidUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUser
public class UserDTO {
    private String id;
    @NotBlank(message = "Username must not be empty or null")
    private String username;
    @NotBlank(message = "First name must not be empty or null")
    private String firstName;
    @NotBlank(message = "Last name must not be empty or null")
    private String lastName;
    @NotBlank(message = "Password must not be empty or null")
    @Size(min = 8, max = 16, message = "Password must be between 8-16 digits")
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}