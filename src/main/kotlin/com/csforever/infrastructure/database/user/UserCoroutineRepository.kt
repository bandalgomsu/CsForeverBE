package com.csforever.infrastructure.database.user

import com.csforever.app.user.model.Position
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserCoroutineRepository : CoroutineCrudRepository<UserEntity, Long> {
    suspend fun findByEmail(email: String): UserEntity?
    suspend fun existsByEmail(email: String): Boolean

    @Query(
        """
        UPDATE users
        SET nickname = :nickname,
            career = :career,
            position = :position
        WHERE id = :userId
    """
    )
    suspend fun update(
        userId: Long,
        nickname: String,
        career: Int,
        position: Position
    ): Int
}