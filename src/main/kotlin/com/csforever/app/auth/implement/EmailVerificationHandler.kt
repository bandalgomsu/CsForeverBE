package com.csforever.app.auth.implement

import com.csforever.app.auth.dto.AuthResponse
import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.model.EmailVerificationPrefix
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.mail.MailValidator
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.scope.CustomScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailVerificationHandler(
    private val redisClient: RedisClient,
    private val mailSender: JavaMailSender
) {
    suspend fun sendSignUpVerificationEmail(email: String, code: String): Boolean {
        MailValidator.validateEmail(email)

        val title = "CS Forever 회원 가입 인증 이메일 입니다."
        val content = "CS Forever에 방문해주셔서 감사합니다." +
                "<br><br>" +
                "회원가입 인증 번호는 " + code + "입니다." +
                "<br>" +
                "인증번호를 제대로 입력해주세요"

        sendMail(email, title, content, code)

        redisClient.deleteData(EmailVerificationPrefix.SIGN_UP.createKey(email))

        return redisClient.setData(
            EmailVerificationPrefix.SIGN_UP.createKey(email), code, 180
        )
    }

    private suspend fun sendMail(toMail: String, title: String, content: String, code: String) {
        val fromMail = "maildevgogo@gmail.com"

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "utf-8")

        helper.setFrom(fromMail)
        helper.setTo(toMail)
        helper.setSubject(title)
        helper.setText(content, true)
        CustomScope.fireAndForget.launch {
            mailSender.send(message)
        }
    }

    suspend fun verifySignUpEmailCode(email: String, code: String): AuthResponse.SignUpVerifyEmailCodeResponse {
        val storedCode = redisClient.getData(EmailVerificationPrefix.SIGN_UP.createKey(email), String::class)
            ?: throw BusinessException(AuthErrorCode.EXPIRED_VERIFICATION_CODE)

        if (storedCode != code) {
            throw BusinessException(AuthErrorCode.INVALID_VERIFICATION_CODE)
        }

        CoroutineScope(Dispatchers.IO).async {
            redisClient.deleteData(EmailVerificationPrefix.SIGN_UP.createKey(email))
            redisClient.setData(EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email), email)
        }.await()

        return AuthResponse.SignUpVerifyEmailCodeResponse(
            email = email,
            verificationCode = code,
            isVerified = true
        )
    }

    suspend fun checkVerifiedEmail(email: String) {
        redisClient.getData(EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email), String::class)
            ?: throw BusinessException(AuthErrorCode.NOT_EXISTS_VERIFICATION_SESSION)
    }

    suspend fun deleteVerifiedSession(email: String) {
        redisClient.deleteData(EmailVerificationPrefix.SIGN_UP_VERIFIED.createKey(email))
    }
}