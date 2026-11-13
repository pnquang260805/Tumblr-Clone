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

    public void deleteUser(String authHeader) throws ParseException, JOSEException {
        String token = jwtUtil.parseAuthHeader(authHeader);
        // đang bị lặp code. khả năng sẽ phải có thêm 1 cái middle ware
        if(!jwtUtil.verifyToken(token)){
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByToken(token);
        postService.deletePosts(user.getPosts());
        userService.deleteUser(user);
    }
}
