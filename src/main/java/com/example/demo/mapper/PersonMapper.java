package com.example.demo.mapper;

import com.example.demo.dto.CreatePerson;
import com.example.demo.model.Person;

public class PersonMapper {

    public static Person mapPerson(CreatePerson dto) {
        return new Person(dto.name(), dto.email(), dto.dateOfBirth());
    }
}
