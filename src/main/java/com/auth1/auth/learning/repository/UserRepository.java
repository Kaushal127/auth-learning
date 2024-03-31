package com.auth1.auth.learning.repository;

import com.auth1.auth.learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User ,Long> {
}
