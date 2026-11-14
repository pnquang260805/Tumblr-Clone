package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.model.Post;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.repository.PostRepository;
import com.qpn.tumblrclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Sửa lại response
@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public User createUser(User u){
        Optional<User> user = userRepository.findByEmail(u.getEmail());
        if(user.isPresent()){
            throw new RuntimeException("User with email " + u.getEmail() + " already existed");
        }

        u.setPassword(passwordEncoder.encode(u.getPassword()));
        userRepository.save(u);
        return u;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User findById(Integer id){
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return u;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(User u){
        userRepository.delete(u);
    }

}
