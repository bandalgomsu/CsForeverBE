package com.csforever.app.domain.user.profile.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.domain.user.profile.dao.UserDao
import com.csforever.app.domain.user.profile.exception.UserErrorCode
import com.csforever.app.domain.user.profile.model.User
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userDao: UserDao
) {

    suspend fun findByEmail(email: String): User {
        return userDao.findByEmail(email) ?: throw BusinessException(UserErrorCode.USER_NOT_FOUND)

    }

    suspend fun findById(userId: Long): User {
        return userDao.findById(userId) ?: throw BusinessException(UserErrorCode.USER_NOT_FOUND)
    }
}