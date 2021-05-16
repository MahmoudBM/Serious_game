package com.value.resource_backend_sg.Services;


import com.value.resource_backend_sg.Models.AuthenticationRequest;
import com.value.resource_backend_sg.Models.ClinicalCase;
import com.value.resource_backend_sg.Models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "backend-sg")
public interface FeignService {

    @GetMapping("/Home")
    String getToken();

    @GetMapping("/users")
    List<User> getAllUser();

    @PostMapping("/auth")
    String userAuthentication(@RequestBody AuthenticationRequest authenticationRequest);

    @PostMapping("/register")
    User addUSer(@RequestBody User user);

    @PostMapping("/addCC")
    ClinicalCase add_Clinical_Case(@RequestParam("statement") String statement, @RequestParam("imageFile") MultipartFile imageFile);




}
