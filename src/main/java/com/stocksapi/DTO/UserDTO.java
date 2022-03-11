package com.stocksapi.DTO;

import com.stocksapi.Validation.ValidUser;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUser
public class UserDTO {
    private String id;
    @NotNull(message = "Account Number must not be null")
    private Integer accountNumber;
    @NotBlank(message = "Name must not be blank")
    @NotNull(message = "Name must not be null")
    private String name;
    @Pattern(regexp = "^[0-9]*$", message = "MemberNumber must only integers")
    @Size(min = 8, max = 8, message = "MemberNumber must be 8 digits")
    private String memberNumber;
    @Pattern(regexp = "^[0-9]*$", message = "MemberID must only integers")
    @Size(min = 5, max = 16, message = "MemberID must be between 5-16 digits")
    private String memberId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}