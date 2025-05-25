package com.csforever.app.common.redis

import kotlin.reflect.KClass

interface RedisClient {
    suspend fun <T> setData(key: String, data: T): Boolean
    suspend fun <T> setData(key: String, data: T, durationSeconds: Long): Boolean
    suspend fun <T> addDataToSet(key: String, data: T): Boolean

    suspend fun <T : Any> getData(key: String, type: KClass<T>): T?
    suspend fun <T : Any> getData(key: String, type: Class<T>): T?
    suspend fun <T : Any> getRandomDataFromSet(key: String, type: KClass<T>): T?
    suspend fun <T : Any> getRandomDataFromSet(key: String, type: Class<T>): T?

    suspend fun deleteData(key: String): Boolean
    suspend fun deleteAllByPattern(pattern: String): Boolean
}