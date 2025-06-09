package com.csforever.app.user.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.user.UserTestUtil
import com.csforever.app.user.dao.UserDao
import com.csforever.app.user.exception.UserErrorCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserFinderTest {
    private val userDao: UserDao = mockk(relaxed = true)
    private val userFinder = UserFinder(userDao)

    @Test
    @DisplayName("이메일 기반 유저 조회 성공")
    fun `Test findByEmail()`() = runTest {
        val user = UserTestUtil.createTestUser(1L)

        coEvery { userDao.findByEmail(user.email) } returns user

        val response = userFinder.findByEmail(user.email)

        assert(response.id == user.id)
        assert(response.email == user.email)
        assert(response.nickname == user.nickname)
        assert(response.role == user.role)
        assert(response.career == user.career)
        assert(response.position == user.position)
        assert(response.password == user.password)
    }

    @Test
    @DisplayName("이메일 기반 유저 조회 실패 (존재하지 않는 유저)")
    fun `Test findByEmail() Throw User Not Found`() = runTest {
        val user = UserTestUtil.createTestUser(1L)

        coEvery { userDao.findByEmail(user.email) } returns null

        val response = assertThrows<BusinessException> { userFinder.findByEmail(user.email) }

        assert(response.errorCode == UserErrorCode.USER_NOT_FOUND)
    }

    @Test
    @DisplayName("Id 기반 유저 조회 성공")
    fun `Test findById()`() = runTest {
        val user = UserTestUtil.createTestUser(1L)

        coEvery { userDao.findById(userId = user.id!!) } returns user

        val response = userFinder.findById(user.id!!)

        assert(response.id == user.id)
        assert(response.email == user.email)
        assert(response.nickname == user.nickname)
        assert(response.role == user.role)
        assert(response.career == user.career)
        assert(response.position == user.position)
        assert(response.password == user.password)
    }

    @Test
    @DisplayName("Id 기반 유저 조회 실패 (존재하지 않는 유저)")
    fun `Test findById() Throw User Not Found`() = runTest {
        val user = UserTestUtil.createTestUser(1L)

        coEvery { userDao.findById(userId = user.id!!) } returns null

        val response = assertThrows<BusinessException> { userFinder.findById(user.id!!) }

        assert(response.errorCode == UserErrorCode.USER_NOT_FOUND)
    }
}