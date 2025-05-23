import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.implement.EmailVerificationHandler
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.mail.javamail.JavaMailSender
import kotlin.test.assertFailsWith


class EmailVerificationHandlerTest {

    private val redisClient: RedisClient = mockk()
    private val mailSender: JavaMailSender = mockk()

    private val emailVerificationHandler = EmailVerificationHandler(redisClient, mailSender)

    @Test
    fun `sendSignUpVerificationEmail sends verification email and stores code`() = runBlocking {
        val email = "test@example.com"
        val code = "123456"

        coEvery { redisClient.setData(any(), any(String::class)) } returns true
        coEvery { redisClient.setData(any(), any(String::class), any()) } returns true
        coEvery { redisClient.deleteData(any()) } returns true

        val mimeMessage = mockk<MimeMessage>(relaxed = true)
        coEvery { mailSender.createMimeMessage() } returns mimeMessage
        coEvery { mailSender.send(mimeMessage) } returns Unit

        val result = emailVerificationHandler.sendSignUpVerificationEmail(email, code)

        assertTrue(result)
        coVerify { redisClient.setData(any(), any(String::class), any()) }
        coVerify { mailSender.send(mimeMessage) }
    }

    @Test
    fun `verifySignUpEmailCode verifies correct code and updates Redis`() = runBlocking {
        val email = "test@example.com"
        val code = "123456"
        val storedCode = "123456"

        coEvery { redisClient.getData(any(), String::class) } returns storedCode
        coEvery { redisClient.deleteData(any()) } returns true
        coEvery { redisClient.setData(any(), any(String::class)) } returns true

        val response = emailVerificationHandler.verifySignUpEmailCode(email, code)

        assertTrue(response.isVerified)
        coVerify { redisClient.deleteData(any()) }
        coVerify { redisClient.setData(any(), any(String::class)) }
    }

    @Test
    fun `verifySignUpEmailCode throws exception when code is invalid`() = runBlocking {
        val email = "test@example.com"
        val code = "123456"
        val storedCode = "654321"

        coEvery { redisClient.getData(any(), String::class) } returns storedCode

        val exception = assertFailsWith<BusinessException> {
            runBlocking {
                emailVerificationHandler.verifySignUpEmailCode(email, code)
            }
        }
        assertEquals(AuthErrorCode.INVALID_VERIFICATION_CODE, exception.errorCode)
    }

    @Test
    fun `checkVerifiedEmail verifies email verification session exists`() = runBlocking {
        val email = "test@example.com"

        coEvery { redisClient.getData(any(), String::class) } returns "verified"

        emailVerificationHandler.checkVerifiedEmail(email)

        coVerify { redisClient.getData(any(), String::class) }
    }

    @Test
    fun `checkVerifiedEmail throws exception when session not exists`() = runBlocking {
        val email = "test@example.com"

        coEvery { redisClient.getData(any(), String::class) } returns null

        val exception = assertFailsWith<BusinessException> {
            runBlocking {
                emailVerificationHandler.checkVerifiedEmail(email)
            }
        }
        assertEquals(AuthErrorCode.NOT_EXISTS_VERIFICATION_SESSION, exception.errorCode)
    }

    @Test
    fun `deleteVerifiedSession deletes verification session`() = runBlocking {
        val email = "test@example.com"

        coEvery { redisClient.deleteData(any()) } returns true

        emailVerificationHandler.deleteVerifiedSession(email)

        coVerify { redisClient.deleteData(any()) }
    }
}

