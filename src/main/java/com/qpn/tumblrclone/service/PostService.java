package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostRequest;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

// Đang lặp code validate user. Có thể làm thêm 1 hàm helper với param: (token, Integer id)

@Service
public class PostService {
    @Autowired private PostRepository postRepository;
    @Autowired private UserService userService;
    @Autowired private JWTUtil jwtUtil;

    public Post createPost(String authHeader, PostRequest request) throws ParseException, JOSEException {
        String token = jwtUtil.parseAuthHeader(authHeader);
        User u = userService.getUserByToken(token);

        Post post = new Post();
        post.setContent(request.getContent());
        post.setUser(u);

        u.getPosts().add(post);

        return postRepository.save(post);
    }

    public Post updatePost(String authHeader, Integer postId, PostRequest request) throws ParseException, JOSEException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not exist"));

        // Xác thực xem người dùng có đúng là chủ post hay không
        // Check bằng compare user id
        String token = jwtUtil.parseAuthHeader(authHeader);
        User u = userService.getUserByToken(token);
        if(u.getId().compareTo(post.getUser().getId()) != 0){
            throw new RuntimeException("Permission denied");
        }

        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    public Post readPost(Integer postId){
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not exist"));
    }

    // Check lại hàm này xem có vi phạm solid không
    public List<Post> readAllPostOfUser(String authHeader) throws ParseException, JOSEException {
        String token = jwtUtil.parseAuthHeader(authHeader);
        User u = userService.getUserByToken(token);
        return u.getPosts();
    }

    public String deletePost(String authHeader, Integer postId) throws ParseException, JOSEException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not exist"));
        String token = jwtUtil.parseAuthHeader(authHeader);
        User u = userService.getUserByToken(token);
        if(u.getId().compareTo(post.getUser().getId()) != 0){
            throw new RuntimeException("Permission denied");
        }
        postRepository.delete(post);
        return "Done";
    }
}
