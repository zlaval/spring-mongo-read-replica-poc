package com.zlrx.springmongoreplica.configuration

import com.mongodb.ReadPreference
import com.mongodb.connection.SslSettings
import org.apache.http.ssl.SSLContextBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.InputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

@Configuration
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = ["cluster-read"], havingValue = "true", matchIfMissing = false)
class MongoReadConfiguration {

    @Bean
    fun customizeMongoReadPreference() = MongoClientSettingsBuilderCustomizer {
        it.readPreference(ReadPreference.nearest())
    }

}

@Configuration
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = ["keystore", "keystore-password"], matchIfMissing = false)
class MongoTlsConfiguration {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${spring.data.mongodb.keystore}")
    private var storePath = "/certs/mongo.jks"

    @Value("\${spring.data.mongodb.keystore-password}")
    private var storePassword = "s3cr3tP@55w0rd"

    @Bean
    fun customizeMongoTls() = MongoClientSettingsBuilderCustomizer {
        it.applyToSslSettings { builder ->
            builder.applySettings(
                SslSettings.builder()
                    .enabled(true)
                    .invalidHostNameAllowed(true)
                    .context(getSslContext())
                    .build()
            )
        }
    }

    private fun getSslContext(): SSLContext? {
        var keyStoreStream: InputStream? = null
        return try {
            keyStoreStream = this.javaClass.classLoader.getResourceAsStream(storePath)
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            tmf.init(null as KeyStore?)
            val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
            trustStore.load(keyStoreStream, storePassword.toCharArray())
            SSLContextBuilder()
                .loadTrustMaterial(trustStore, null)
                .build()

        } catch (e: Exception) {
            logger.error(e.message, e)
            throw RuntimeException(e)
        } finally {
            keyStoreStream?.close()
        }
    }

}
