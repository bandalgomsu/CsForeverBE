package com.csforever.app.domain.user.profile.implement

import com.csforever.app.domain.user.profile.dao.UserDao
import com.csforever.app.domain.user.profile.model.Position
import org.springframework.stereotype.Component

@Component
class UserUpdater(
    private val userDao: UserDao
) {

    suspend fun updateUserProfile(userId: Long, nickname: String, career: Int, position: Position) {
        userDao.update(
            userId = userId,
            nickname = nickname,
            career = career,
            position = position
        )
    }
}