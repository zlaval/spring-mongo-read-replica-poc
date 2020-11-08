package com.zlrx.springmongoreplica.repository

import com.zlrx.springmongoreplica.domain.Person
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PersonRepository : CoroutineCrudRepository<Person, String> {
}