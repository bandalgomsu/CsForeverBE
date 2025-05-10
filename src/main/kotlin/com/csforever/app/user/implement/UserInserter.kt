package com.csforever.app.user.implement

import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import org.springframework.stereotype.Component

@Component
class UserInserter(
    private val userDao: UserDao
) {

    suspend fun insert(
        email: String,
        password: String,
        role: Role,
        nickname: String,
        career: Int = 0,
        position: Position,
    ): User {
        val user = User(
            email = email,
            password = password,
            role = role,
            nickname = nickname,
            career = career,
            position = position,
        )

        return userDao.insert(user)
    }
}