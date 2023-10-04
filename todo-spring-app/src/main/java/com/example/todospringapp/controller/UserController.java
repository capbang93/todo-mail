package com.example.todospringapp.controller;

import com.example.todospringapp.dto.ResponseDto;
import com.example.todospringapp.dto.UserDto;
import com.example.todospringapp.model.UserEntity;
import com.example.todospringapp.security.TokenProvider;
import com.example.todospringapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {
    final private UserService userService;
    final private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/profile")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal String userId){
        try{
            UserEntity registeredUser = userService.getInfo(userId);
            UserDto responseUserDto = UserDto.builder()
                    .email(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal String userId,
                                        @RequestBody UserDto dto){
        try{
            UserEntity registeredUser = userService.updateInfo(userId, dto);
            UserDto responseUserDto = UserDto.builder()
                    .email(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        try{
            UserEntity user = UserEntity.builder()
                    .email(userDto.getEmail())
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();

            UserEntity registeredUser = userService.create(user);
            UserDto responseUserDto = UserDto.builder()
                    .email(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .id(registeredUser.getId())
                    .build();

            return ResponseEntity.ok().body(responseUserDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto) {
        UserEntity user = userService.getByCredentials(userDto.getEmail(), userDto.getPassword(), passwordEncoder);

        if(user != null){
            // 로그인한 사용자 정보로 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDto responseUserDto = UserDto.builder()
                    .email(user.getEmail())
                    .id(userDto.getId())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDto);
        } else {
            ResponseDto responseDto = ResponseDto.builder()
                    .error("Login failed")
                    .build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
