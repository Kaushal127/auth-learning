package com.auth1.auth.learning.service;

import com.auth1.auth.learning.dtos.SendEmailMessageDto;
import com.auth1.auth.learning.model.Token;
import com.auth1.auth.learning.model.User;
import com.auth1.auth.learning.repository.TokenRepository;
import com.auth1.auth.learning.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private KafkaTemplate<String , String> kafkaTemplate ;
    @Autowired
    private ObjectMapper objectMapper ;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder ;
    @Autowired
    private TokenRepository tokenRepository;
    public User signUp(String email , String password, String name) {
        User user = new User() ;
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User saveUser = userRepository.save(user) ;

        SendEmailMessageDto message = new SendEmailMessageDto();
        message.setFrom("support@scaler.com");
        message.setTo(email);
        message.setSubject("Welcome to Laundry Hub...!!!");
        message.setBody("Hey looking forward to serve You..!! For Laundry Pick Up call 8380858085.. Thanks..!! ");

        try {
            kafkaTemplate.send(
                    "sendEmail",
                    objectMapper.writeValueAsString(message)
            ) ;
        } catch (Exception e) {

        }
        return saveUser ;
    }

    public Token login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email) ;
        if(userOptional.isEmpty()){
            throw new RuntimeException("Invalid User or Password ") ;
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException("Invalid User or Password ") ;
        }

        Token token = new Token();
        token.setUser(user);
        token.setValue(UUID.randomUUID().toString());

        Date expireDate = getExpiredDate();
        token.setExpireAt(expireDate);
        tokenRepository.save(token) ;
        return token ;
    }

    // expiration date is 30 days from today
    private Date getExpiredDate() {
        Calendar calendarDate = Calendar.getInstance() ;
        calendarDate.setTime(new Date());

        calendarDate.add(Calendar.DAY_OF_MONTH, 30);
        Date expiryDate = calendarDate.getTime() ;
        return expiryDate ;

    }

    public void logout(String token) {
        //
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedEquals(token , false ) ;
        if(tokenOptional.isEmpty()){
            throw new RuntimeException("Token is invalid") ;
        }

        Token tokenObject = tokenOptional.get();
        tokenObject.setDeleted(true);

        tokenRepository.save(tokenObject) ;
    }

    public boolean validateToken(String token) {
        /*
        1. check if token value is present
        2. check if token is not deleted
        3. check if token is not expired
        */
       Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedEqualsAndExpireAtGreaterThan(
                token,false , new Date()
        ) ;

        if (tokenOptional.isEmpty()) {
            return false ;
        }
        return true ;
    }

    public ResponseEntity<User> getUserWithEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return null ;
        }
    }

 /*   public Optional<User> getUserWithEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user;
        }
        return null ;
    }*/
}
