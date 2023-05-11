package com.banking.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "UserName should not be empty")
    private String userName;
    @NotBlank(message = "Password should not be empty")
    private String password;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Email should not empty")
    private String email;
    private Long mobileNumber;

    private String gender;
    @Past(message = "Date of Birth should be past")
    private LocalDate dateOfBirth;

}
