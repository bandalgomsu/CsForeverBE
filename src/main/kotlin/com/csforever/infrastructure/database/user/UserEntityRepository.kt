package com.csforever.infrastructure.database.user

import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.User
import org.springframework.stereotype.Repository

@Repository
class UserEntityRepository(
    private val userCoroutineRepository: UserCoroutineRepository
) : UserDao {

    override suspend fun findByEmail(email: String): User? {
        return userCoroutineRepository.findByEmail(email)?.toModel()
    }

    override suspend fun findById(userId: Long): User? {
        return userCoroutineRepository.findById(userId)?.toModel()
    }

    override suspend fun existsByEmail(email: String): Boolean {
        return userCoroutineRepository.existsByEmail(email)
    }

    override suspend fun insert(user: User): User {
        return userCoroutineRepository.save(
            UserEntity(
                email = user.email,
                password = user.password,
                role = user.role,
                nickname = user.nickname,
                career = user.career,
                position = user.position,
            )
        ).toModel()
    }

    override suspend fun update(userId: Long, nickname: String, career: Int, position: Position) {
        userCoroutineRepository.update(
            userId = userId,
            nickname = nickname,
            career = career,
            position = position
        )
    }
}