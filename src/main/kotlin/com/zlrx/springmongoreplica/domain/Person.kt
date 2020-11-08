package com.zlrx.springmongoreplica.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Person(
    val _id: String? = null,
    val name: String,
    val age: Int
)