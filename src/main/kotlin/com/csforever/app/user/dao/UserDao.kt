package com.csforever.app.user.dao

import com.csforever.app.user.model.Position
import com.csforever.app.user.model.User

interface UserDao {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(userId: Long): User?

    suspend fun existsByEmail(email: String): Boolean
    suspend fun insert(user: User): User
    suspend fun update(userId: Long, nickname: String, career: Int, position: Position)
}