package com.qpn.tumblrclone.common;

import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.DTO.UserResponse;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class Mapping {
//    @Bean Để thì nó sẽ có autowired
    public PostResponse postMapping(Post post){
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setUserId(post.getUser().getId());
        return response;
    }

    public UserResponse userResponseMapping(User user){
        UserResponse response = new UserResponse();
        response.setPostResponses(user.getPosts().stream().map(x -> postMapping(x)).toList());
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        return response;
    }
}
