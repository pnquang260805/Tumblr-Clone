package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostRequest;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class PostService {
    @Autowired private PostRepository postRepository;
    @Autowired private UserService userService;

    public Post createPost(String authHeader, PostRequest request) throws ParseException, JOSEException {
        String token = authHeader;
        if(authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        User u = userService.getUserByToken(token);

        Post post = new Post();
        post.setContent(request.getContent());
        post.setUser(u);

        u.getPosts().add(post);

        return postRepository.save(post);
    }
}
