package com.qpn.tumblrclone.controller;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostRequest;
import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.common.Mapping;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private Mapping mapping;
    @Autowired
    private JWTUtil jWTUtil;

    @PostMapping
    public PostResponse createPost(@RequestHeader("Authorization") String authHeader,
                                   @RequestBody PostRequest request) throws ParseException, JOSEException {
        Post post = postService.createPost(authHeader, request);
        return mapping.postMapping(post);
    }

    @GetMapping // All by user
    public List<PostResponse> allPostByUser(@RequestHeader("Authorization") String authHeader) throws ParseException, JOSEException {
        List<Post> posts = postService.readAllPostOfUser(authHeader);
        List<PostResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            responses.add(mapping.postMapping(post));
        }
        return responses;
    }

    @PutMapping
    public PostResponse updatePost(@RequestHeader("Authorization") String authHeader,
                                   @RequestParam Integer id,
                                   @RequestBody PostRequest request) throws ParseException, JOSEException {
        // Token lưu ở máy người dùng --> Khả năng là sẽ ko có người nào (không phải hacker) có được mà truyền vào
        Post post = postService.updatePost(authHeader, id, request);
        return mapping.postMapping(post);
    }

    @DeleteMapping
    public String deletePost(@RequestHeader("Authorization") String authHeader,
                             @RequestParam Integer id) throws ParseException, JOSEException {
        return postService.deletePost(authHeader, id);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Integer id){
        return mapping.postMapping(postService.readPost(id));
    }
}
