package com.csforever.user.implement

import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.implement.UserInserter
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserInserterTest {
    private val userDao: UserDao = mockk(relaxed = true)

    private val userInserter: UserInserter = UserInserter(userDao)


    @Test
    fun insertTest() = runBlocking {
        val user = User(
            id = 1L,
            email = "testEmail",
            password = "testPassword",
            nickname = "testNickname",
            career = 1,
            role = Role.USER,
            position = Position.BACKEND
        )

        coEvery { userDao.insert(any()) } returns user

        val response = userInserter.insert(
            email = "testEmail",
            password = "testPassword",
            role = Role.USER,
            nickname = "testNickname",
            career = 1,
            position = Position.BACKEND
        )

        assertEquals(user.id, response.id)
        assertEquals(user.email, response.email)
        assertEquals(user.password, response.password)
        assertEquals(user.nickname, response.nickname)
        assertEquals(user.career, response.career)
        assertEquals(user.role, response.role)
        assertEquals(user.position, response.position)
    }
}