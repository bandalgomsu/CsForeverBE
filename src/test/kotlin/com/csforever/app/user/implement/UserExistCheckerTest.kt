package com.csforever.app.user.implement

import com.csforever.app.user.dao.UserDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserExistCheckerTest {
    private val userDao: UserDao = mockk(relaxed = true)
    private val userExistChecker = UserExistChecker(userDao)

    @Test
    @DisplayName("이메일 기반 유저 존재 유무 확인")
    fun `Test isExistByEmail()`() = runTest {
        val email = "test@test.com"

        coEvery { userDao.existsByEmail(email) } returns true

        val response = userExistChecker.isExistByEmail(email)

        assertTrue(response)
    }
}