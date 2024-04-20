package com.auth1.auth.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @PostMapping ("/hi")
    public String sayHello(){
        return "Hello" ;
    }

    @GetMapping("/Kaushal")
    public String getMessage(){
        return "Kaushal , Sr No 211 , Bhekrainagar , Fursungi , Pune 412308" ;
    }
}
