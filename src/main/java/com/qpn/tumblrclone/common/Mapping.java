package com.qpn.tumblrclone.common;

import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.model.Post;
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
}
