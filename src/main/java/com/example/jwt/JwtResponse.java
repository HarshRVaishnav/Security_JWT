package com.example.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private  String token;
    private final String type = "Bearer";

    public JwtResponse(String token,String type) {
        this.token=token;
    }
}
