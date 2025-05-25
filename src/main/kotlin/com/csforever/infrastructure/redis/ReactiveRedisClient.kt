package com.csforever.infrastructure.redis

import com.csforever.app.common.redis.RedisClient
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import kotlin.reflect.KClass

@Component
class ReactiveRedisClient(
    @Qualifier("reactiveRedisStringTemplate") private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : RedisClient {
    override suspend fun <T> setData(key: String, data: T): Boolean = coroutineScope {
        redisTemplate.opsForValue()
            .set(key, objectMapper.writeValueAsString(data))
            .awaitSingle()
    }

    override suspend fun <T> setData(key: String, data: T, durationSeconds: Long): Boolean = coroutineScope {
        redisTemplate.opsForValue()
            .set(key, objectMapper.writeValueAsString(data), Duration.ofSeconds(durationSeconds))
            .awaitSingle()
    }

    override suspend fun <T> addDataToSet(key: String, data: T): Boolean {
        redisTemplate.opsForSet()
            .add(key, objectMapper.writeValueAsString(data))
            .awaitSingle()

        return true
    }

    override suspend fun <T : Any> getData(key: String, type: KClass<T>): T? = coroutineScope {
        redisTemplate.opsForValue()
            .get(key)
            .awaitSingleOrNull()
            ?.let {
                objectMapper.readValue(it, type.java)
            }
    }

    override suspend fun <T : Any> getData(key: String, type: Class<T>): T? = coroutineScope {
        redisTemplate.opsForValue()
            .get(key)
            .awaitSingleOrNull()
            ?.let {
                objectMapper.readValue(it, type)
            }
    }

    override suspend fun <T : Any> getRandomDataFromSet(key: String, type: KClass<T>): T? {
        return redisTemplate.opsForSet()
            .randomMember(key)
            .awaitSingleOrNull()
            ?.let {
                objectMapper.readValue(it, type.java)
            }
    }

    override suspend fun <T : Any> getRandomDataFromSet(key: String, type: Class<T>): T? {
        return redisTemplate.opsForSet()
            .randomMember(key)
            .awaitSingleOrNull()
            ?.let {
                objectMapper.readValue(it, type)
            }
    }

    override suspend fun deleteData(key: String): Boolean = coroutineScope {
        redisTemplate.opsForValue()
            .delete(key)
            .awaitSingle()
    }

    override suspend fun deleteAllByPattern(pattern: String): Boolean = coroutineScope {
        val keys = redisTemplate.keys(pattern).asFlow()

        keys.collect {
            launch {
                redisTemplate.delete(it).awaitSingle()
            }
        }

        return@coroutineScope true
    }
}