package com.example.demo.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreatePerson;
import com.example.demo.dto.UpdatePerson;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    
    public Page<Person> getAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }


    public Optional<Person> getPersonById(String id) {
        return personRepository.findById(id);
    }

    @Transactional
    public Person createPerson(CreatePerson dto) {
        Person person = PersonMapper.mapPerson(dto);
        return personRepository.save(person);
    }

    @Transactional
    public Optional<Person> updatePerson(String id, UpdatePerson updatedPerson) {
        return personRepository.findById(id).map(existingPerson -> {
            existingPerson.setName(updatedPerson.name());
            existingPerson.setEmail(updatedPerson.email());
            existingPerson.setDateOfBirth(updatedPerson.dateOfBirth());
            return personRepository.save(existingPerson);
        });
    }

    @Transactional
    public boolean deletePerson(String id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
