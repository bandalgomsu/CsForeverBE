package com.csforever.app.domain.user.profile.implement

import com.csforever.app.domain.user.profile.dao.UserDao
import org.springframework.stereotype.Component

@Component
class UserExistChecker(
    private val userDao: UserDao
) {

    suspend fun isExistByEmail(email: String): Boolean {
        return userDao.existsByEmail(email)
    }
}