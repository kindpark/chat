package com.creative.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creative.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

