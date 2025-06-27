package com.csforever.app.auth.implement

import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.domain.user.auth.model.EmailVerificationPrefix
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.domain.user.auth.implement.EmailVerificationHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mail.javamail.JavaMailSender
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmailVerificationHandlerTest {
    private val redisClient: RedisClient = mockk(relaxed = true)
    private val mailSender: JavaMailSender = mockk(relaxed = true)

    private val emailVerificationHandler = EmailVerificationHandler(redisClient, mailSender)

    @Test
    @DisplayName("회원가입 인증 메일 전송")
    fun `Test sendSignUpVerificationEmail`() = runTest {
        val email = "test@test.com"
        val code = "123456"

        coEvery { redisClient.deleteData(EmailVerificationPrefix.SIGN_UP.createKey(email)) } returns true
        coEvery { redisClient.setData(EmailVerificationPrefix.SIGN_UP.createKey(email), code, 180) } returns true

        val response = emailVerificationHandler.sendSignUpVerificationEmail(email, code)

        assertTrue(response)
    }

    @Test
    @DisplayName("회원가입 인증 메일 검증 성공")
    fun `Test verifySignUpEmailCode`() = runTest {
        val email = "test@test.com"
        val code = "123456"

        coEvery { redisClient.getData(EmailVerificationPrefix.SIGN_UP.createKey(email), String::class) } returns code

        coEvery { redisClient.deleteData(EmailVerificationPrefix.SIGN_UP.createKey(email)) } returns true
        coEvery { redisClient.setData(EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email), email) } returns true

        val response = emailVerificationHandler.verifySignUpEmailCode(email, code)

        coVerify(exactly = 1) {
            redisClient.setData(
                EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email),
                email,
                1200
            )
        }
        coVerify(exactly = 1) { redisClient.deleteData(EmailVerificationPrefix.SIGN_UP.createKey(email)) }

        assertEquals(email, response.email)
        assertEquals(code, response.verificationCode)
        assertEquals(true, response.isVerified)
    }

    @Test
    @DisplayName("회원가입 인증 메일 검증 실패 (코드 만료)")
    fun `Test verifySignUpEmailCode Throw EXPIRED_VERIFICATION_CODE`() = runTest {
        val email = "test@test.com"
        val code = "123456"

        coEvery { redisClient.getData(EmailVerificationPrefix.SIGN_UP.createKey(email), String::class) } returns null

        val response = assertThrows<BusinessException> {
            emailVerificationHandler.verifySignUpEmailCode(email, code)
        }

        assertEquals(AuthErrorCode.EXPIRED_VERIFICATION_CODE, response.errorCode)
    }

    @Test
    @DisplayName("회원가입 인증 메일 검증 실패 (잘못된 코드)")
    fun `Test verifySignUpEmailCode Throw INVALID_VERIFICATION_CODE`() = runTest {
        val email = "test@test.com"
        val code = "123456"
        val storedCode = "333333"

        coEvery {
            redisClient.getData(
                EmailVerificationPrefix.SIGN_UP.createKey(email),
                String::class
            )
        } returns storedCode

        val response = assertThrows<BusinessException> {
            emailVerificationHandler.verifySignUpEmailCode(email, code)
        }

        assertEquals(AuthErrorCode.INVALID_VERIFICATION_CODE, response.errorCode)
    }

    @Test
    @DisplayName("인증 메일 세션 확인 성공")
    fun `Test checkVerifiedEmail`() = runTest {
        val email = "test@test.com"

        coEvery {
            redisClient.getData(
                EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email),
                String::class
            )
        } returns email

        emailVerificationHandler.checkVerifiedEmail(email)

        coVerify(exactly = 1) {
            redisClient.getData(
                EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email),
                String::class
            )
        }
    }

    @Test
    @DisplayName("인증 메일 세션 확인 실패 (인증 세션 없음)")
    fun `Test checkVerifiedEmail Throw NOT_EXISTS_VERIFICATION_SESSION`() = runTest {
        val email = "test@test.com"

        coEvery {
            redisClient.getData(
                EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email),
                String::class
            )
        } returns null

        val response = assertThrows<BusinessException> {
            emailVerificationHandler.checkVerifiedEmail(email)
        }

        assertEquals(AuthErrorCode.NOT_EXISTS_VERIFICATION_SESSION, response.errorCode)
    }

    @Test
    @DisplayName("인증 메일 세션 삭제")
    fun `Test deleteVerifiedSession`() = runTest {
        val email = "test@test.com"

        coEvery { redisClient.deleteData(EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email)) } returns true

        emailVerificationHandler.deleteVerifiedSession(email)

        coVerify(exactly = 1) { redisClient.deleteData(EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email)) }
    }
}