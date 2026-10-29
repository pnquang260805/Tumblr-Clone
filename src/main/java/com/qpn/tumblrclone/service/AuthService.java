package com.qpn.tumblrclone.service;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.LoginRequest;
import com.qpn.tumblrclone.DTO.LoginResponse;
import com.qpn.tumblrclone.common.JWTUtil;
import com.qpn.tumblrclone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private JWTUtil jwtUtil;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    public LoginResponse loginUser(LoginRequest request) throws JOSEException {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userService.findByEmail(email);
        boolean passwordCorrect = passwordEncoder.matches(password, user.getPassword());
        if(!passwordCorrect){
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtUtil.generateToken(user.getId());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
}
