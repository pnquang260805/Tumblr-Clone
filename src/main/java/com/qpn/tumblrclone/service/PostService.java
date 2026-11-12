package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.PostRequest;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Đang lặp code validate user. Có thể làm thêm 1 hàm helper với param: (token, Integer id)

@Service
public class PostService {
    @Autowired private PostRepository postRepository;
    @Autowired private UserService userService;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private S3Service s3Service;

    public Post createPost(String authHeader, PostRequest request) throws ParseException, JOSEException {
        String token = jwtUtil.parseAuthHeader(authHeader);
        User u = userService.getUserByToken(token);

        Post post = new Post();
        String postId = UUID.randomUUID().toString();
        post.setId(postId);
        post.setContent(request.getContent());
        post.setUser(u);
        post.setPostedTime(LocalDateTime.now());

//        u.getPosts().add(post);
        // Post là bên sở hữu quan hệ (có @JoinColumn), chỉ cần post.setUser(u) là DB lưu đúng user_id: ????????????
        for(MultipartFile f : request.getMediaFiles()){
            String fileName = s3Service.generateFileName(u.getId(), postId, f);
            try{
                s3Service.uploadToS3(fileName, f);
                post.getMediaUrl().add(fileName);
            } catch (Exception e){
                throw new RuntimeException("Error when upload file " + fileName);
            }
        }

        return postRepository.save(post);
    }

    public Post updatePost(String authHeader, String postId, PostRequest request) throws ParseException, JOSEException {
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

    public Post readPost(String postId){
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not exist"));
    }

    // Check lại hàm này xem có vi phạm solid không
    public List<Post> readAllPostOfUser(String authHeader) throws ParseException, JOSEException {
        String token = jwtUtil.parseAuthHeader(authHeader);
        User u = userService.getUserByToken(token);
        return u.getPosts();
    }

    public String deletePost(String authHeader, String postId) throws ParseException, JOSEException {
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
