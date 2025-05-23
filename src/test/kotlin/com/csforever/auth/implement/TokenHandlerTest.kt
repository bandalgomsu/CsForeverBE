import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.auth.model.UserAuthorizationContext
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TokenHandlerTest {

    private val redisClient: RedisClient = mockk()
    private val expireSeconds: Long = 3600 // 예시로 1시간 만료 시간 설정

    private val tokenHandler = TokenHandler(redisClient, expireSeconds)

    @Test
    fun `generate generates a new token and stores it in Redis`() = runBlocking {
        val user = User(
            id = 1L,
            email = "test_email",
            password = "test_password",
            nickname = "test_nickname",
            role = Role.USER,
            career = 1,
            position = Position.BACKEND
        )

        val token = UUID.randomUUID().toString()

        coEvery { redisClient.setData(any(), any(String::class), expireSeconds) } returns true

        // When
        val response = tokenHandler.generate(user)

        // Then
        assertNotNull(response.token)
        coVerify { redisClient.setData("session:${response.token}", any(String::class), expireSeconds) }
    }

    @Test
    fun `validateToken validates a valid token and refreshes expiration time`() = runBlocking {
        val user = User(
            id = 1L,
            email = "test_email",
            password = "test_password",
            nickname = "test_nickname",
            role = Role.USER,
            career = 1,
            position = Position.BACKEND
        )

        val token = "validToken"
        val authorization = UserAuthorizationContext(user, user.role, token)

        coEvery { redisClient.getData("session:$token", UserAuthorizationContext::class) } returns authorization
        coEvery { redisClient.setData("session:$token", authorization, expireSeconds) } returns true

        val result = tokenHandler.validateToken(token)

        assertEquals(authorization, result)
        coVerify { redisClient.setData("session:$token", authorization, expireSeconds) }
    }

    @Test
    fun `validateToken throws exception when token is invalid`() = runBlocking {

        val token = "invalidToken"

        coEvery { redisClient.getData("session:$token", UserAuthorizationContext::class) } returns null


        val exception = assertThrows<BusinessException> {
            runBlocking {
                tokenHandler.validateToken(token)
            }
        }
        assertEquals(AuthErrorCode.NOT_LOGIN_USER, exception.errorCode)
    }

    @Test
    fun `extractToken returns empty context if token doesn't exist`() = runBlocking {
        val token = "nonExistentToken"

        coEvery { redisClient.getData("session:$token", UserAuthorizationContext::class) } returns null
        val result = tokenHandler.extractToken(token)

        assertEquals(UserAuthorizationContext(), result)
        coVerify(exactly = 0) { redisClient.setData("session:$token", result, expireSeconds) }
    }

    @Test
    fun `deleteToken deletes token from Redis`() = runBlocking {
        val token = "tokenToDelete"

        coEvery { redisClient.deleteData("session:$token") } returns true

        tokenHandler.deleteToken(token)

        coVerify { redisClient.deleteData("session:$token") }
    }
}
