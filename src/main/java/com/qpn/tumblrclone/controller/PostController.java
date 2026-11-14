package com.qpn.tumblrclone.controller;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostRequest;
import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.common.Mapping;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.service.PostService;
import com.qpn.tumblrclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired private PostService postService;
    @Autowired private Mapping mapping;
    @Autowired private JWTUtil jwtUtil;

    private Integer getUserIdFromAuthHeader(String authHeader) throws ParseException {
        String token = authHeader;
        if(authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        Map<String, Object> payload = jwtUtil.getPayload(token);
        return Integer.valueOf(payload.get("userId").toString());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponse createPost(@RequestHeader("Authorization") String authHeader,
                                   @ModelAttribute PostRequest request) throws ParseException { // Xem lại cái này
        Integer userId = getUserIdFromAuthHeader(authHeader);
        Post post = postService.createPost(userId, request);
        return mapping.postMapping(post);
    }

    @PutMapping
    public PostResponse updatePost( @RequestParam String postId, // pre-condition: đã login
                                   @RequestBody PostRequest request){
        Post post = postService.updatePost(postId, request);
        return mapping.postMapping(post);
    }

    @DeleteMapping
    public String deletePost(@RequestParam String id){
        postService.deletePost(id);
        return "Deleted post";
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable String id){
        return mapping.postMapping(postService.readPost(id));
    }
}
