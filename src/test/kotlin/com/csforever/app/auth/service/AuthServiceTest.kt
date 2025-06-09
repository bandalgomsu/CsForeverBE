package com.csforever.app.auth.service

import com.csforever.app.auth.dto.AuthRequest
import com.csforever.app.auth.dto.AuthResponse
import com.csforever.app.auth.dto.TokenResponse
import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.implement.EmailVerificationHandler
import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.common.crypto.CryptoUtil
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.user.UserTestUtil
import com.csforever.app.user.implement.UserExistChecker
import com.csforever.app.user.implement.UserFinder
import com.csforever.app.user.implement.UserInserter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AuthServiceTest {
    private val userFinder: UserFinder = mockk(relaxed = true)
    private val userExistChecker: UserExistChecker = mockk(relaxed = true)
    private val userInserter: UserInserter = mockk(relaxed = true)
    private val tokenHandler: TokenHandler = mockk(relaxed = true)
    private val emailVerificationHandler: EmailVerificationHandler = mockk(relaxed = true)

    private val authService = AuthService(
        userFinder,
        userExistChecker,
        userInserter,
        tokenHandler,
        emailVerificationHandler
    )

    @Test
    @DisplayName("로그인 성공")
    fun `Test login`() = runTest {
        val password = "password123"
        val storedPassword = CryptoUtil.hashPassword(password)
        val userId = 1L

        val user = UserTestUtil.createTestUser(userId, storedPassword)
        val tokenValue = "token"

        val token = TokenResponse(
            token = tokenValue
        )

        val request = AuthRequest.LoginRequest(
            email = user.email,
            password = password
        )

        coEvery { userFinder.findByEmail(request.email) } returns user
        coEvery { tokenHandler.generate(user) } returns token

        val response = authService.login(request)

        assert(response.token == tokenValue)
    }

    @Test
    @DisplayName("로그인 실패 (비밀번호 틀림)")
    fun `Test login Throw NOT_MATCHED_PASSWORD`() = runTest {
        val password = "password123"
        val storedPassword = "differentPassword"
        val userId = 1L

        val user = UserTestUtil.createTestUser(userId, storedPassword)
        val tokenValue = "token"

        val request = AuthRequest.LoginRequest(
            email = user.email,
            password = password
        )

        coEvery { userFinder.findByEmail(request.email) } returns user

        val response = assertThrows<BusinessException> {
            authService.login(request)
        }

        assert(response.errorCode == AuthErrorCode.NOT_MATCHED_PASSWORD)
    }

    @Test
    @DisplayName("로그아웃")
    fun `Test logout`() = runTest {
        val token = "token"

        coEvery { tokenHandler.deleteToken(token) } returns Unit

        authService.logout(token)

        coVerify(exactly = 1) { tokenHandler.deleteToken(token) }
    }

    @Test
    @DisplayName("회원가입 인증 메일 전송")
    fun `Test sendSignUpVerificationEmail`() = runTest {
        val email = "test@test.com"

        coEvery { emailVerificationHandler.sendSignUpVerificationEmail(email, any()) } returns true

        authService.sendSignUpVerificationEmail(email)

        coVerify(exactly = 1) { emailVerificationHandler.sendSignUpVerificationEmail(email, any()) }
    }

    @Test
    @DisplayName("회원가입 인증 메일 코드 검증")
    fun `Test verifySignUpEmailCode`() = runTest {
        val email = "test@test.com"
        val code = "123456"

        val responseData = AuthResponse.SignUpVerifyEmailCodeResponse(
            email = email,
            verificationCode = code,
            isVerified = true
        )

        coEvery { emailVerificationHandler.verifySignUpEmailCode(email, code) } returns responseData

        val response = authService.verifySignUpEmailCode(email, code)

        assert(response.email == email)
        assert(response.verificationCode == code)
        assert(response.isVerified)
    }

    @Test
    @DisplayName("회원가입 성공")
    fun `Test signUp`() = runTest {
        val userId = 1L
        val password = "password"
        val storedPassword = CryptoUtil.hashPassword(password)

        val user = UserTestUtil.createTestUser(userId, storedPassword)

        val request = AuthRequest.SignUpRequest(
            email = user.email,
            password = password,
            nickname = "nickname",
            career = user.career,
            position = user.position
        )

        val tokenValue = "token"
        val token = TokenResponse(
            token = tokenValue
        )

        coEvery { emailVerificationHandler.checkVerifiedEmail(request.email) } returns Unit
        coEvery { userExistChecker.isExistByEmail(request.email) } returns false
        coEvery {
            userInserter.insert(
                request.email,
                storedPassword,
                user.role,
                request.nickname,
                request.career,
                request.position
            )
        } returns user
        coEvery { tokenHandler.generate(any()) } returns token
        coEvery { emailVerificationHandler.deleteVerifiedSession(request.email) } returns Unit

        val response = authService.signUp(request)

        assertEquals(tokenValue, response.token)
    }

    @Test
    @DisplayName("회원가입 실패")
    fun `Test signUp Throw NOT_EXISTS_VERIFICATION_SESSION`() = runTest {
        val userId = 1L
        val password = "password"
        val storedPassword = CryptoUtil.hashPassword(password)

        val user = UserTestUtil.createTestUser(userId, storedPassword)

        val request = AuthRequest.SignUpRequest(
            email = user.email,
            password = password,
            nickname = "nickname",
            career = user.career,
            position = user.position
        )

        coEvery { emailVerificationHandler.checkVerifiedEmail(request.email) } throws BusinessException(AuthErrorCode.NOT_EXISTS_VERIFICATION_SESSION)

        val response = assertThrows<BusinessException> {
            authService.signUp(request)
        }

        coVerify(exactly = 0) { userExistChecker.isExistByEmail(request.email) }
        coVerify(exactly = 0) {
            userInserter.insert(
                request.email,
                storedPassword,
                user.role,
                request.nickname,
                request.career,
                request.position
            )
        }
        coVerify(exactly = 0) { tokenHandler.generate(any()) }
        coVerify(exactly = 0) { emailVerificationHandler.deleteVerifiedSession(request.email) }
    }

    @Test
    @DisplayName("회원가입 실패 (이미 등록된 사용자)")
    fun `Test signUp Throw ALREADY_REGISTERED_USER`() = runTest {
        val userId = 1L
        val password = "password"
        val storedPassword = CryptoUtil.hashPassword(password)

        val user = UserTestUtil.createTestUser(userId, storedPassword)

        val request = AuthRequest.SignUpRequest(
            email = user.email,
            password = password,
            nickname = "nickname",
            career = user.career,
            position = user.position
        )
        
        coEvery { emailVerificationHandler.checkVerifiedEmail(request.email) } returns Unit
        coEvery { userExistChecker.isExistByEmail(request.email) } returns true

        val response = assertThrows<BusinessException> {
            authService.signUp(request)
        }

        assert(response.errorCode == AuthErrorCode.ALREADY_REGISTERED_USER)

        coVerify(exactly = 0) {
            userInserter.insert(
                request.email,
                storedPassword,
                user.role,
                request.nickname,
                request.career,
                request.position
            )
        }
        coVerify(exactly = 0) { tokenHandler.generate(any()) }
        coVerify(exactly = 0) { emailVerificationHandler.deleteVerifiedSession(request.email) }
    }
}