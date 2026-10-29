package com.qpn.tumblrclone.controller;

import com.nimbusds.jose.JOSEException;
import com.qpn.tumblrclone.DTO.LoginRequest;
import com.qpn.tumblrclone.DTO.LoginResponse;
import com.qpn.tumblrclone.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws JOSEException {
        return authService.loginUser(request);
    }
}
