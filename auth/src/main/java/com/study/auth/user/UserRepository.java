package com.study.auth.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
}
