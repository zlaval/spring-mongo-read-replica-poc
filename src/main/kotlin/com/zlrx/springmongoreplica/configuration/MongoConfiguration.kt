package com.zlrx.springmongoreplica.configuration

import com.mongodb.ReadPreference
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfiguration {

    @Bean
    fun customizeMongoReadPreference() = MongoClientSettingsBuilderCustomizer {
        it.readPreference(ReadPreference.nearest())
    }

}