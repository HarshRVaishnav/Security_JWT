package com.example.controller;


import com.example.customresponse.CustomResponse;
import com.example.entity.Customer;
import com.example.exception.CustomerNotFoundException;
import com.example.jwt.JwtRequest;
import com.example.jwt.JwtResponse;
import com.example.jwt.SignUpRequest;
import com.example.service.ICustomerService;
import com.example.serviceimpl.UserService;
import com.example.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private String code;

    private Object data;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "Welcome to Jwt";
    }

    @PostMapping("/login")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CustomerNotFoundException("INVALID_CREDENTIALS " + e);
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody SignUpRequest customer) {
        try {
            Customer customer1 = customerService.registerCustomerSignUp(customer);
            data = customer1;
            code = "CREATED";
        } catch (CustomerNotFoundException customerNotFoundException) {
            code = "DATA_NOT_CREATED";
            data = null;
        } catch (RuntimeException runtimeException) {
            code = "RUNTIME_EXCEPTION";
            data = null;
        } catch (Exception exception) {
            code = "EXCEPTION";
            data = null;
        } finally {
            return CustomResponse.response(code, data);
        }
    }
}