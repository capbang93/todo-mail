package com.example.todospringapp.dto;

import com.example.todospringapp.model.TodoEntity;
import com.example.todospringapp.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String token;
    private String email;
    private String username;
    private String password;
    private String id;
}
