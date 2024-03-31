package com.auth1.auth.learning.controller;

import com.auth1.auth.learning.dtos.SignupRequestDto;
import com.auth1.auth.learning.model.User;
import com.auth1.auth.learning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService ;

    @PostMapping("/signup")
    public User signUp(@RequestBody SignupRequestDto requestDto){
        return userService.signUp(requestDto.getEmail() ,
                requestDto.getPassword(),requestDto.getName());
    }
}
