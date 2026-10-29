package com.qpn.tumblrclone.controller;

import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user") // Đây mới là routing
public class UserController {
    @Autowired private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User u){
        return userService.createUser(u);
    }

    @GetMapping
    public List<User> findAllUser(){
        return userService.getAllUser();
    }
}
