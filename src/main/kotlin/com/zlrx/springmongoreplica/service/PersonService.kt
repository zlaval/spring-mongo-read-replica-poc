package com.zlrx.springmongoreplica.service

import com.zlrx.springmongoreplica.domain.Person
import com.zlrx.springmongoreplica.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service

@Service
class PersonService constructor(
    private val personRepository: PersonRepository,
    private val mongoTemplate: ReactiveMongoTemplate
) {

    suspend fun findAllAdult(): Flow<Person> {
        return personRepository.findAll().filter {
            it.age > 18
        }
    }

    suspend fun save(person: Person): Person {
        return personRepository.save(person)
    }

    suspend fun changeStream(): Flow<Person> {
        return flow {
            val options = ChangeStreamOptions.builder()
                .returnFullDocumentOnUpdate()
                .build()
            mongoTemplate.changeStream("Person", options, Person::class.java)
                .map { it.body }
                .log()
        }
    }

}