package com.csforever.app.user.implement

import com.csforever.app.user.dao.UserDao
import org.springframework.stereotype.Component

@Component
class UserExistChecker(
    private val userDao: UserDao
) {

    suspend fun isExistByEmail(email: String): Boolean {
        return userDao.existsByEmail(email)
    }
}