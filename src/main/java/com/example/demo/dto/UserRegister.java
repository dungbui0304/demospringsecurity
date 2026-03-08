package com.example.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegister {
    private String username;
    private String password;
    private List<String> roles;
}
