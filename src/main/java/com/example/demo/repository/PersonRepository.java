package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Person;

public interface PersonRepository extends MongoRepository<Person, String> {

}
