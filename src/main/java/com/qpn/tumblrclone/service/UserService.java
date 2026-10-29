package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JWTUtil jwtUtil;

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

    public User getUserByToken(String token) throws ParseException, JOSEException {
        if(!jwtUtil.verifyToken(token)){
            throw new RuntimeException("Invalid token");
        }
        Map<String, Object> payload = jwtUtil.getPayload(token);
        // Bước 1: Ép về Number (lớp cha chung của Long và Integer)
        Number userIdNumber = (Number) payload.get("userId");

        // Bước 2: Chuyển sang Integer để truyền vào findById
        Integer userId = userIdNumber.intValue();
        User user = findById(userId);
        return user;
    }
}
