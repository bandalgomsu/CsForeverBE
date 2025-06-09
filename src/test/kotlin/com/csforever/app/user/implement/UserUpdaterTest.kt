package com.csforever.app.user.implement

import com.csforever.app.user.UserTestUtil
import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.model.Position
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserUpdaterTest {
    private val userDao: UserDao = mockk(relaxed = true)
    private val userUpdater = UserUpdater(userDao)

    @Test
    @DisplayName("유저 프로필 업데이트 성공")
    fun `Test updateUserProfile()`() = runTest {
        val user = UserTestUtil.createTestUser(1L)

        coEvery {
            userDao.update(
                userId = user.id!!,
                nickname = "newNickname",
                career = 5,
                position = Position.AI
            )
        } returns Unit

        userUpdater.updateUserProfile(
            userId = user.id!!,
            nickname = "newNickname",
            career = 5,
            position = Position.AI
        )

        coVerify(exactly = 1) {
            userDao.update(
                userId = user.id!!,
                nickname = "newNickname",
                career = 5,
                position = Position.AI
            )
        }
    }
}