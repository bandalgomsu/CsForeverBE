package com.csforever.app.domain.user.profile.implement

import com.csforever.app.domain.user.profile.dao.UserDao
import com.csforever.app.domain.user.profile.model.Position
import com.csforever.app.domain.user.profile.model.Role
import com.csforever.app.domain.user.profile.model.User
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