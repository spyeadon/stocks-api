package com.springgradlesandbox.springgradlesandox.Domain;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull(message = "Account Number must not be null")
    private Integer accountNumber;
    @NotBlank(message = "Client Name must not be blank")
    @NotNull(message = "Client Name must not be null")
    private String clientName;
}