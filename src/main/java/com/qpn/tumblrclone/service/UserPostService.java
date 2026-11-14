package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class UserPostService {
    // Dùng để xử lý Circular Dependencies
    @Autowired private UserService userService;
    @Autowired private PostService postService;
    @Autowired private JWTUtil jwtUtil;

    public void deleteUser(Integer userId){
        User user = userService.findById(userId);
        postService.deletePosts(user.getPosts());
        userService.deleteUser(user);
    }
}
