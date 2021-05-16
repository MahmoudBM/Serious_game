package com.value.resource_backend_sg.Models;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
    private String role;
    private String firstname;
    private String lastname;

}
