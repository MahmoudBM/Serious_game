package com.value.resource_backend_sg.Controllers;

import com.value.resource_backend_sg.Models.Authentication;
import com.value.resource_backend_sg.Models.AuthenticationRequest;
import com.value.resource_backend_sg.Models.User;
import com.value.resource_backend_sg.Repository.UserRepositoryFeign;
import com.value.resource_backend_sg.Services.UserServiceFeign;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserRepositoryFeign userRepositoryFeign;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Autowired
    private PasswordEncoder passwordEncoder;



    /******** Home page with username **********/
    @GetMapping("/Home")
    private String getToken() {
        return "Welcome to home page " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /******** Add moderator **********/
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> addUSer(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userServiceFeign.addUser(user);
    }

    /******** Get all moderators **********/
    @GetMapping("/users")
    public List<User> getAllUser() {
        return userServiceFeign.getAllUsers();
    }

    /******** Test **********/
    @PostMapping("/test")
    private String userRegister(@RequestBody AuthenticationRequest authenticationRequest){
        log.info("registering user");
        String username = authenticationRequest.getUsername();
        String password = passwordEncoder.encode(authenticationRequest.getPassword());
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        try {
            userRepositoryFeign.save(user);
        } catch (Exception e){
            return "Registration failed";
        }

        return username+" Registered successfully";
    }

    @PostMapping(value = "/auth")
    private Authentication userAuthentication(@RequestBody AuthenticationRequest authenticationRequest) throws JSONException {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        String createPersonUrl = "http://localhost:8102/auth";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("username",username);
        personJsonObject.put("password", password);
        HttpEntity<String> request =
                new HttpEntity<String>(personJsonObject.toString(), headers);

        String personResultAsJsonStr =
                restTemplate.postForObject(createPersonUrl, request, String.class);

        log.info("generated token: {}", personResultAsJsonStr);
            return Authentication.builder().accessToken(personResultAsJsonStr)
                .build();

        //System.out.println(personResultAsJsonStr);
        //return personResultAsJsonStr;
         }



    /******** Test **********/
    @PostMapping("/testAPI")
    private String  testAPI(@RequestBody AuthenticationRequest authenticationRequest){

        String testUsername = authenticationRequest.getUsername();
        String testPassword = passwordEncoder.encode(authenticationRequest.getPassword());
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        try {
            userRepositoryFeign.save(user);

        } catch (Exception e){
            return "Test failed";

        }

        return testUsername+" Test Successed ";
    }

}
