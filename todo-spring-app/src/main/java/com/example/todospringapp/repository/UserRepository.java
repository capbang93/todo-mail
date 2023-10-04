package com.example.todospringapp.repository;

import com.example.todospringapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);
}
