package com.springgradlesandbox.springgradlesandox.Domain;

import com.springgradlesandbox.springgradlesandox.Validation.ValidUser;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUser
public class UserDTO {
    @NotNull(message = "Account Number must not be null")
    private Integer accountNumber;
    @NotBlank(message = "Client Name must not be blank")
    @NotNull(message = "Client Name must not be null")
    private String clientName;
    @Pattern(regexp = "^[0-9]*$", message = "MemberNumber must only integers")
    @Size(min = 8, max = 8, message = "MemberNumber must be 8 digits")
    private String memberNumber;
    @Pattern(regexp = "^[0-9]*$", message = "MemberID must only integers")
    @Size(min = 5, max = 16, message = "MemberID must be between 5-16 digits")
    private String memberId;
}