package com.example.todospringapp.service;

import com.example.todospringapp.dto.UserDto;
import com.example.todospringapp.model.UserEntity;
import com.example.todospringapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity){
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            log.warn("Email already exists");
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

    public UserEntity getInfo(final String userId) {
        final UserEntity user = userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("사용자를 찾을 수 없음"));

        return user;
    }

    public UserEntity updateInfo(String userId, UserDto dto) {
        final UserEntity user = userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("사용자를 찾을 수 없음"));
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());

        userRepository.save(user);

        return user;
    }
}
