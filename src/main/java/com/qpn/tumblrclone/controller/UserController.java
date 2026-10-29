package com.qpn.tumblrclone.controller;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.UserResponse;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/user") // Đây mới là routing
public class UserController {
    @Autowired private UserService userService;

    @PostMapping("/register")
    public UserResponse createUser(@RequestBody User u){
        User user = userService.createUser(u);
        UserResponse response = new UserResponse();
        response.setDisplayName(user.getDisplayName());
        response.setEmail(user.getEmail());
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return response;
    }

    @GetMapping
    public List<User> findAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/me")
    public UserResponse findMe(@RequestHeader("Authorization") String authHeader) throws ParseException, JOSEException {
        String token = authHeader;
        if(authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        User user = userService.getUserByToken(token);
        UserResponse response = new UserResponse();
        response.setDisplayName(user.getDisplayName());
        response.setEmail(user.getEmail());
        response.setId(user.getId());
        response.setUsername(user.getUsername());

        return response;
    }
}
