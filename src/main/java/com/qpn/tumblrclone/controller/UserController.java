package com.qpn.tumblrclone.controller;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.DTO.UserResponse;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.common.Mapping;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.service.UserPostService;
import com.qpn.tumblrclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user") // Đây mới là routing
public class UserController {
    @Autowired private UserService userService;
    @Autowired private Mapping mapping;
    @Autowired private UserPostService userPostService;
    @Autowired private JWTUtil jwtUtil;

    private Integer getUserIdFromAuthHeader(String authHeader) throws ParseException {
        String token = authHeader;
        if(authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        Map<String, Object> payload = jwtUtil.getPayload(token);
        return Integer.valueOf(payload.get("userId").toString());
    }

    @PostMapping("/register")
    public UserResponse createUser(@RequestBody User u){
        User user = userService.createUser(u);
        return mapping.userResponseMapping(user);
    }

    @GetMapping
    public List<UserResponse> findAllUser(){
        return userService.getAllUser().stream().map(user -> mapping.userResponseMapping(user)).toList();
    }

    @GetMapping("/me")
    public UserResponse findMe(@RequestHeader("Authorization") String authHeader) throws ParseException, JOSEException {
        Integer userId = getUserIdFromAuthHeader(authHeader);
        User user = userService.findById(userId);
        return mapping.userResponseMapping(user);
    }

    @DeleteMapping
    public String deleteUser(@RequestHeader("Authorization") String authHeader) throws ParseException, JOSEException {
        Integer userId = getUserIdFromAuthHeader(authHeader);
        userPostService.deleteUser(userId);
        return "Done";
    }
}
