import com.csforever.app.auth.dto.AuthRequest
import com.csforever.app.auth.dto.AuthResponse
import com.csforever.app.auth.dto.TokenResponse
import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.implement.EmailVerificationHandler
import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.auth.service.AuthService
import com.csforever.app.common.crypto.CryptoUtil
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.user.implement.UserExistChecker
import com.csforever.app.user.implement.UserFinder
import com.csforever.app.user.implement.UserInserter
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthServiceTest {

    private val userFinder: UserFinder = mockk()
    private val userExistChecker: UserExistChecker = mockk()
    private val userInserter: UserInserter = mockk()
    private val tokenHandler: TokenHandler = mockk()
    private val emailVerificationHandler: EmailVerificationHandler = mockk()

    private val authService =
        AuthService(userFinder, userExistChecker, userInserter, tokenHandler, emailVerificationHandler)

    @Test
    fun `login should return token when credentials are correct`() = runBlocking {
        val email = "test@example.com"
        val password = "password123"
        val request = AuthRequest.LoginRequest(email, password)
        val user = User(
            id = 1L,
            email = email,
            password = CryptoUtil.hashPassword(request.password),
            nickname = "test_nickname",
            role = Role.USER,
            career = 1,
            position = Position.BACKEND
        )
        val tokenResponse = TokenResponse("token123")

        coEvery { userFinder.findByEmail(email) } returns user
        coEvery { tokenHandler.generate(user) } returns tokenResponse

        val response = authService.login(request)

        assertEquals("token123", response.token)
        coVerify { tokenHandler.generate(user) }
    }

    @Test
    fun `login should throw exception when password is incorrect`() = runBlocking {
        val email = "test@example.com"
        val password = "wrongPassword"
        val request = AuthRequest.LoginRequest(email, password)
        val user = User(
            id = 1L,
            email = email,
            password = CryptoUtil.hashPassword("test"),
            nickname = "test_nickname",
            role = Role.USER,
            career = 1,
            position = Position.BACKEND
        )

        coEvery { userFinder.findByEmail(email) } returns user

        val exception = assertThrows<BusinessException> {
            runBlocking {
                authService.login(request)
            }
        }
        assertEquals(AuthErrorCode.NOT_MATCHED_PASSWORD, exception.errorCode)
    }

    @Test
    fun `logout should delete token`() = runBlocking {
        // Given
        val token = "token123"

        coEvery { tokenHandler.deleteToken(token) } returns Unit

        // When
        authService.logout(token)

        // Then
        coVerify { tokenHandler.deleteToken(token) }
    }

    @Test
    fun `sendSignUpVerificationEmail should send email`() = runBlocking {
        val email = "test@example.com"
        val code = "123456"

        coEvery { emailVerificationHandler.sendSignUpVerificationEmail(email, code) } returns true

        authService.sendSignUpVerificationEmail(email, code)

        coVerify { emailVerificationHandler.sendSignUpVerificationEmail(email, code) }
    }

    @Test
    fun `verifySignUpEmailCode should verify email code`() = runBlocking {
        val email = "test@example.com"
        val code = "123456"
        val response = AuthResponse.SignUpVerifyEmailCodeResponse(email, code, true)

        coEvery { emailVerificationHandler.verifySignUpEmailCode(email, code) } returns response

        val result = authService.verifySignUpEmailCode(email, code)

        assertTrue(result.isVerified)
        coVerify { emailVerificationHandler.verifySignUpEmailCode(email, code) }
    }

    @Test
    fun `signUp should register user and return token`() = runBlocking {
        // Given
        val request = AuthRequest.SignUpRequest(
            email = "test@example.com",
            password = "password123",
            nickname = "nickname",
            career = 1,
            position = Position.BACKEND
        )
        val hashedPassword = "hashedPassword"
        val user = User(
            id = 1L,
            email = request.email,
            password = CryptoUtil.hashPassword(request.password),
            nickname = request.nickname,
            role = Role.USER,
            career = request.career,
            position = request.position
        )
        val tokenResponse = TokenResponse("token123")

        coEvery { emailVerificationHandler.checkVerifiedEmail(request.email) } returns Unit
        coEvery { userExistChecker.isExistByEmail(request.email) } returns false
        coEvery {
            userInserter.insert(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns user
        coEvery { tokenHandler.generate(user) } returns tokenResponse
        coEvery { emailVerificationHandler.deleteVerifiedSession(request.email) } returns Unit
        val response = authService.signUp(request)

        assertEquals("token123", response.token)
        coVerify { tokenHandler.generate(user) }
    }

    @Test
    fun `signUp should throw exception when user is already registered`() = runBlocking {
        // Given
        val request = AuthRequest.SignUpRequest(
            email = "test@example.com",
            password = "password123",
            nickname = "nickname",
            career = 1,
            position = Position.BACKEND
        )

        coEvery { emailVerificationHandler.checkVerifiedEmail(request.email) } returns Unit
        coEvery { userExistChecker.isExistByEmail(request.email) } returns true

        // When & Then
        val exception = assertThrows<BusinessException> {
            runBlocking {
                authService.signUp(request)
            }
        }
        assertEquals(AuthErrorCode.ALREADY_REGISTERED_USER, exception.errorCode)
    }
}
