package com.csforever.app.user.implement

import com.csforever.app.user.UserTestUtil
import com.csforever.app.domain.user.profile.dao.UserDao
import com.csforever.app.domain.user.profile.implement.UserInserter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserInserterTest {
    private val userDao: UserDao = mockk(relaxed = true)
    private val userInserter = UserInserter(userDao)

    @Test
    @DisplayName("유저 삽입 성공")
    fun `Test Insert()`() = runTest {
        val user = UserTestUtil.createTestUser(1L)

        coEvery { userDao.insert(any()) } returns user

        val response = userInserter.insert(
            email = user.email,
            password = user.password,
            role = user.role,
            nickname = user.nickname,
            career = user.career,
            position = user.position
        )

        assert(response.email == user.email)
        assert(response.password == user.password)
        assert(response.role == user.role)
        assert(response.nickname == user.nickname)
        assert(response.career == user.career)
        assert(response.position == user.position)
    }
}