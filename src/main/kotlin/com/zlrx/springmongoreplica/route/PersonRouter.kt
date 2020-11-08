package com.zlrx.springmongoreplica.route

import com.zlrx.springmongoreplica.domain.Person
import com.zlrx.springmongoreplica.service.PersonService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class PersonRouter constructor(
    private val personService: PersonService
) {

    @Bean
    fun personRoute() = coRouter {
        "/api/v1/person".nest {
            GET("/adult") {
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .bodyAndAwait(personService.findAllAdult())
            }
            POST("") {
                val person = it.awaitBody<Person>()
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .bodyValueAndAwait(personService.save(person))
            }
            GET("") {
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .bodyAndAwait(personService.changeStream())
            }

        }
    }


}