package com.example.jwt;

import com.example.customvalidation.PostalCode;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SignUpRequest {

    @Hidden
    private Integer customerNumber;

    @Size(min = 3, max = 15, message = "Customer First Name Must be between 3 to 15")
    @Pattern(regexp = "^[A-Z a-z]*$", message = "Invalid Input")
    private String customerFirstName;

    @Size(min = 3, max = 15, message = "Customer Last Name Must be between 3 to 15")
    @Pattern(regexp = "^[A-Z a-z]*$", message = "Invalid Input")
    private String customerLastName;

    @Pattern(regexp = "^[0-9]{10}", message = "Invalid Mobile Number")
    private String phone;

    @NotBlank(message = "Address line 1 cannot be blank")
    private String addressLine1;

    @Size(max = 50, message = "Address line 2 cannot be more than 50 characters")
    private String addressLine2;

    @NotBlank(message = "City cannot be blank")
    @Pattern(regexp = "^[A-Z a-z]*$", message = "Invalid Input")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Pattern(regexp = "^[A-Z a-z]*$", message = "Invalid Input")
    private String state;

    @PostalCode
    private Integer postalCode;

    @NotBlank(message = "Country cannot be blank")
    @Pattern(regexp = "^[A-Z a-z]*$", message = "Invalid Input")
    private String country;

    @Size(min = 3, max = 15, message = "Customer First Name Must be between 3 to 15")
    @Pattern(regexp = "^[A-Z a-z]*$", message = "Invalid Input")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @NonNull
    private String email;

    @Size(min = 6, max = 120, message = "password Must be between 6 to 120")
    private String password;

    private Set<String> roles;
}