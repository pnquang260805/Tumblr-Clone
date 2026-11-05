package com.qpn.tumblrclone.controller;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostRequest;
import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.common.Mapping;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired private PostService postService;
    @Autowired private Mapping mapping;

    @PostMapping
    public PostResponse createPost(@RequestHeader("Authorization") String authHeader, @RequestBody PostRequest request) throws ParseException, JOSEException {
        Post post = postService.createPost(authHeader, request);
        return mapping.postMapping(post);
    }
}
