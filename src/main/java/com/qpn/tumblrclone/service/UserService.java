package com.qpn.tumblrclone.service;

import com.qpn.tumblrclone.model.User;
import com.qpn.tumblrclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;

    public User createUser(User u){
        userRepository.save(u);
        return u;
    }
}
