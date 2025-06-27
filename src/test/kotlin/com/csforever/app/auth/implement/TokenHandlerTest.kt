package com.csforever.app.auth.implement

import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.domain.user.auth.implement.TokenHandler
import com.csforever.app.user.UserTestUtil
import com.csforever.app.domain.user.profile.model.Role
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNull

class TokenHandlerTest {
    private val redisClient: RedisClient = mockk(relaxed = true)
    private val tokenHandler = TokenHandler(redisClient, 3600)

    @Test
    @DisplayName("Token 생성")
    fun `Test Generate()`() = runTest {
        val userId = 1L
        val user = UserTestUtil.createTestUser(userId)

        coEvery {
            redisClient.setData(
                any(),
                any(UserAuthorizationContext::class),
                any()
            )
        } returns true

        val tokenResponse = tokenHandler.generate(user)

        coVerify(exactly = 1) {
            redisClient.setData(
                any(),
                any(UserAuthorizationContext::class),
                any()
            )
        }
        assert(tokenResponse.token.isNotEmpty())
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    fun `Test validateToken()`() = runTest {
        val token = "test"
        val context = UserAuthorizationContext(
            user = UserTestUtil.createTestUser(1L),
            role = UserTestUtil.createTestUser(1L).role,
            token = token
        )

        coEvery {
            redisClient.getData("session:$token", UserAuthorizationContext::class)
        } returns context

        coEvery {
            redisClient.setData("session:$token", context, 3600)
        } returns true

        val response = tokenHandler.validateToken(token)

        coVerify(exactly = 1) {
            redisClient.setData("session:$token", context, 3600)
        }

        assert(response.user!!.id == context.user!!.id)
        assert(response.role == context.role)
        assert(response.token == context.token)
    }

    @Test
    @DisplayName("토큰 유효성 검사 실패")
    fun `Test validateToken() Throw Not Login User`() = runTest {
        val token = "test"
        val context = UserAuthorizationContext(
            user = UserTestUtil.createTestUser(1L),
            role = UserTestUtil.createTestUser(1L).role,
            token = token
        )

        coEvery {
            redisClient.getData("session:$token", UserAuthorizationContext::class)
        } returns null


        val response = assertThrows<BusinessException> { tokenHandler.validateToken(token) }

        assert(response.errorCode == AuthErrorCode.NOT_LOGIN_USER)
    }

    @Test
    @DisplayName("토큰 추출")
    fun `Test extractToken()`() = runTest {
        val token = "test"
        val context = UserAuthorizationContext(
            user = UserTestUtil.createTestUser(1L),
            role = UserTestUtil.createTestUser(1L).role,
            token = token
        )

        coEvery {
            redisClient.getData("session:$token", UserAuthorizationContext::class)
        } returns context

        coEvery {
            redisClient.setData("session:$token", context, 3600)
        } returns true

        val response = tokenHandler.extractToken(token)

        coVerify(exactly = 1) {
            redisClient.setData("session:$token", context, 3600)
        }

        assert(response.user!!.id == context.user!!.id)
        assert(response.role == context.role)
        assert(response.token == context.token)
    }

    @Test
    @DisplayName("토큰 추출 (존재하지 않는 토큰)")
    fun `Test extractToken() Not Found Token`() = runTest {
        val token = "test"
        val context = UserAuthorizationContext(
            user = UserTestUtil.createTestUser(1L),
            role = UserTestUtil.createTestUser(1L).role,
            token = token
        )

        coEvery {
            redisClient.getData("session:$token", UserAuthorizationContext::class)
        } returns null

        val response = tokenHandler.extractToken(token)

        assertNull(response.token)
        assertNull(response.user)
        assert(Role.GUEST == response.role)
    }

    @Test
    @DisplayName("토큰 삭제")
    fun `Test deleteToken()`() = runTest {
        val token = "delete_token"

        coEvery { redisClient.deleteData("session:$token") } returns true

        tokenHandler.deleteToken(token)

        coVerify(exactly = 1) { redisClient.deleteData("session:$token") }
    }
}