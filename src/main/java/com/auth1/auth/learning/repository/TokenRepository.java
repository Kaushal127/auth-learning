package com.auth1.auth.learning.repository;

import com.auth1.auth.learning.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token ,Long> {

    Optional<Token> findByValueAndDeletedEquals(String token, Boolean isDeleted);

     /*
        1. check if token value is present
        2. check if token is not deleted
        3. check if token is not expired
        */


    Optional<Token> findByValueAndDeletedEqualsAndExpireAtGreaterThan(
            String token, Boolean isDeleted, Date date);

}
