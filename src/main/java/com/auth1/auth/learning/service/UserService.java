package com.auth1.auth.learning.service;

import com.auth1.auth.learning.model.User;
import com.auth1.auth.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder ;
    public User signUp(String email , String password, String name) {
        User user = new User() ;
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user) ;
    }
}
