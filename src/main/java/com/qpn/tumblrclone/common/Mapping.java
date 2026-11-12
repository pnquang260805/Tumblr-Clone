package com.qpn.tumblrclone.common;

import com.qpn.tumblrclone.DTO.PostResponse;
import com.qpn.tumblrclone.DTO.UserResponse;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Mapping {
//    @Bean Để thì nó sẽ có autowired
    @Autowired private S3Service s3Service;
    public PostResponse postMapping(Post post){
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setUserId(post.getUser().getId());
        response.setPostedTime(post.getPostedTime());
        // ???????????????????????????????
        response.setMediaUrl(post.getMediaUrl().stream().map(s3Service::generatePresignedUrl).toList());
        return response;
    }

    public UserResponse userResponseMapping(User user){
        UserResponse response = new UserResponse();
        // Tại sao???????????????????????
        List<Post> posts = user.getPosts() != null ? user.getPosts() : List.of(); // fix lỗi Cannot invoke "java.util.List.stream()" because the return value of "com.qpn.tumblrclone.model.User.getPosts()" is null
        response.setPostResponses(posts.stream().map(this::postMapping).toList());
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        return response;
    }
}
