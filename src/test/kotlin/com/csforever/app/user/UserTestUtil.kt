package com.csforever.app.user

import com.csforever.app.user.dto.UserProfileResponse
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User

class UserTestUtil {
    companion object {

        fun createTestUser(userId: Long? = null, password: String = "password123"): User {
            return User(
                id = userId,
                email = "test@test.com",
                nickname = "test_nickname",
                password = password,
                role = Role.USER,
                career = 1,
                position = Position.BACKEND
            )
        }

        fun createTestUserProfile(user: User): UserProfileResponse.UserProfile {
            return UserProfileResponse.UserProfile.create(
                user = user,
                correctSubmissionCount = 10,
                submissionCount = 20,
                ranking = 1
            )
        }
    }
}