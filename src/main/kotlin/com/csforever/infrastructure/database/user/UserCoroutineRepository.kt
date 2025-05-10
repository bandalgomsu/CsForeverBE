package com.csforever.infrastructure.database.user

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserCoroutineRepository : CoroutineCrudRepository<UserEntity, Long> {
    suspend fun findByEmail(email: String): UserEntity?
    suspend fun existsByEmail(email: String): Boolean
}