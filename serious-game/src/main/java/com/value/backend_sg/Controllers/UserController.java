package com.value.backend_sg.Controllers;

import com.value.backend_sg.Models.AuthenticationRequest;
import com.value.backend_sg.Models.User;
import com.value.backend_sg.Repository.UserRepository;
import com.value.backend_sg.Services.UserService;
import com.value.backend_sg.Utils.JwtUtils;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /******** Home page with username **********/
    @GetMapping("/Home")
    private String getToken(){
        return "Welcome to home page " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /******** Add moderator **********/
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String addUSer(@RequestBody User user) {

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userService.addUser(user);

    }

    /******** Get all moderators **********/
    @GetMapping("/users")
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    /******** Test **********/
    @PostMapping("/test")
    private String  userRegister(@RequestBody AuthenticationRequest authenticationRequest){

        String username = authenticationRequest.getUsername();
        String password = passwordEncoder.encode(authenticationRequest.getPassword());
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        try {
            userRepository.save(user);

        } catch (Exception e){
            return "Registration failed";

        }

        return username+" Registered successfully";
    }

    /******** Authentication **********/
    @PostMapping(value = "/auth")
    private ResponseEntity userAuthentication(@RequestBody AuthenticationRequest authenticationRequest) throws JSONException {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        } catch (Exception e){

            return ResponseEntity.ok("Authentication failed : Wrong credentials ");
        }

        UserDetails loadedUser = userService.loadUserByUsername(username);
        String generatedToken = jwtUtils.generateToken(loadedUser);
        User user = userRepository.findByUsername(username);
        if (Objects.equals(user.getRole(),"moderateur")){
         return ResponseEntity.ok("Bienvenue sur la page modérateur " + user.getFirstname() +" "+ generatedToken);
          //  return ResponseEntity("{\"Bienvenue sur la page modérateur :\"}", HttpStatus.OK);
          //  return ResponseEntity.ok().body(new JSONObject("ezds"));

        }
        else
           // return ResponseEntity.ok().body(new JSONObject("ezds"));

        //   return ResponseEntity("{\"Bienvenue sur la page modérateur :\"}", HttpStatus.OK);
          return   ResponseEntity.ok( "Bievenue sur la page admin " + user.getFirstname() +" "+generatedToken );
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
            userRepository.save(user);

        } catch (Exception e){
            return "Test failed";

        }

        return testUsername+" Test Successed ";
    }

}
