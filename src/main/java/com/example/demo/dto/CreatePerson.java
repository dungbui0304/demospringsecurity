package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreatePerson(@Valid String name,
                           @NotEmpty @Email String email,
                           @NotNull @PastOrPresent LocalDate dateOfBirth
                        //    @NotNull @Valid CreateAddressDTO address,
                        ) {
}