package com.csforever.user.implement

import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.implement.UserExistChecker
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class UserExistCheckerTest {

    private val userDao: UserDao = mockk(relaxed = true)

    private val userExistChecker = UserExistChecker(userDao)

    @Test
    fun isExistEmailTest() = runBlocking {
        val email = "test_email"

        coEvery { userExistChecker.isExistByEmail(email) } returns true

        val response = userExistChecker.isExistByEmail(email)

        assert(response)
    }
}