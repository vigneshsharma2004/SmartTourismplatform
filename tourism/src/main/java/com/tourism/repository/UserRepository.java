package com.tourism.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
