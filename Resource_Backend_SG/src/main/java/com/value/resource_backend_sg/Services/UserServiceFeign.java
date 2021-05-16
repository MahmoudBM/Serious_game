package com.value.resource_backend_sg.Services;

import com.value.resource_backend_sg.Models.User;
import com.value.resource_backend_sg.Repository.UserRepositoryFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceFeign implements UserDetailsService {

    @Autowired
    private UserRepositoryFeign userRepositoryFeign;

    /******** Find user by username **********/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundedUser= userRepositoryFeign.findByUsername(username);
        if(foundedUser ==null) return null;
        String name = foundedUser.getUsername();
        String pwd  = foundedUser.getPassword();
        return new org.springframework.security.core.userdetails.User(name,pwd,new ArrayList<>());
    }
    /******** add moderators **********/
    public ResponseEntity<User> addUser(User user) {
        try {
            User existingUser = userRepositoryFeign.findByUsername(user.getUsername());
            if (existingUser == null) {
                User savedUser = userRepositoryFeign.save(user);
                return ResponseEntity.ok(User
                        .builder()
                        .phone(savedUser.getPhone())
                        .firstname(savedUser.getFirstname())
                        .username(savedUser.getUsername())
                        .role(savedUser.getRole())
                        .lastname(savedUser.getLastname())
                .build());
            }
            //throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
            //if (!userRepositoryFeign.existsByUsername(user.getUsername())) {
            //    userRepositoryFeign.save(user);
            //  return "Welcome "+ user.getFirstname()+" " +user.getLastname();
            //} else {
            //  return "This user "+user.getUsername() +" already exist";
            //}

        } catch (Exception e) {
            throw e;
        }
    }

    /******** Get all moderators **********/
    public List<User> getAllUsers() {

        try {
            return userRepositoryFeign.findAll()
                    .stream()
                    .filter(user -> user.getRole().equalsIgnoreCase("moderateur"))
                    .collect(Collectors.toList());

            /*
            List<User> users = userRepositoryFeign.findAll();
            List<User> customUser = new ArrayList<>();
            users.stream().forEach(u -> {
                User user = new User();
                BeanUtils.copyProperties(u, user);
                if(Objects.equals(u.getRole(),"moderateur"))
                {
                    System.out.println(user.getUsername());
                    customUser.add(user);
                }
            });
            return customUser;

             */
        } catch (Exception e) {
            throw e;
        }
    }
}
