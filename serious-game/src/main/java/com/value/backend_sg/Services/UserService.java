package com.value.backend_sg.Services;

import com.value.backend_sg.Models.Medicament;
import com.value.backend_sg.Models.User;
import com.value.backend_sg.Repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /******** Find user by username **********/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     User foundedUser= userRepository.findByUsername(username);
        if(foundedUser ==null) return null;
        String name = foundedUser.getUsername();
        String pwd  = foundedUser.getPassword();
        return new org.springframework.security.core.userdetails.User(name,pwd,new ArrayList<>());
    }
    /******** add moderators **********/
    public String addUser(User user) {
        try {
            if (!userRepository.existsByUsername(user.getUsername())) {
                userRepository.save(user);
                return "Welcome"+ user.getFirstname()+" " +user.getLastname();
            } else {
                return "This user already exist";
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /******** Get all moderators **********/
    public List<User> getAllUsers() {

        try {
            List<User> users = userRepository.findAll();
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
        } catch (Exception e) {
            throw e;
        }
    }
}
