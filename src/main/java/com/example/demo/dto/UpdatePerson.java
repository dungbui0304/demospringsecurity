package com.example.demo.dto;


import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UpdatePerson(String name,
                            @Email String email,
                            LocalDate dateOfBirth) {
}
