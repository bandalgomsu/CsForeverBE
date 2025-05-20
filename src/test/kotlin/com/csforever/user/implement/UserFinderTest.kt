package com.csforever.user.implement

import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.implement.UserFinder
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserFinderTest {
    private val userDao: UserDao = mockk(relaxed = true)

    private val userFinder = UserFinder(userDao)

    @Test
    fun findByEmailTest() = runBlocking {
        val email = "testEmail"

        val user = User(
            id = 1L,
            email = email,
            password = "testPassword",
            nickname = "testNickname",
            career = 1,
            role = Role.USER,
            position = Position.BACKEND
        )

        coEvery { userDao.findByEmail(email) } returns user

        val response = userFinder.findByEmail(email)

        assertEquals(user.id, response.id)
        assertEquals(user.email, response.email)
        assertEquals(user.password, response.password)
        assertEquals(user.nickname, response.nickname)
        assertEquals(user.career, response.career)
        assertEquals(user.role, response.role)
        assertEquals(user.position, response.position)
    }

    @Test
    fun findByIdTest() = runBlocking {
        val id = 1L
        val user = User(
            id = id,
            email = "testEmail",
            password = "testPassword",
            nickname = "testNickname",
            career = 1,
            role = Role.USER,
            position = Position.BACKEND
        )

        coEvery { userDao.findById(id) } returns user

        val response = userFinder.findById(id)

        assertEquals(user.id, response.id)
        assertEquals(user.email, response.email)
        assertEquals(user.password, response.password)
        assertEquals(user.nickname, response.nickname)
        assertEquals(user.career, response.career)
        assertEquals(user.role, response.role)
        assertEquals(user.position, response.position)
    }
}