package com.csforever.app.auth.service

import com.csforever.app.auth.dto.AuthRequest
import com.csforever.app.auth.dto.AuthResponse
import com.csforever.app.auth.dto.TokenResponse
import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.implement.EmailVerificationHandler
import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.common.crypto.CryptoUtil
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.mail.RandomNumberGenerator
import com.csforever.app.user.implement.UserExistChecker
import com.csforever.app.user.implement.UserFinder
import com.csforever.app.user.implement.UserInserter
import com.csforever.app.user.model.Role
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userFinder: UserFinder,
    private val userExistChecker: UserExistChecker,
    private val userInserter: UserInserter,

    private val tokenHandler: TokenHandler,
    private val emailVerificationHandler: EmailVerificationHandler
) {

    suspend fun login(request: AuthRequest.LoginRequest): TokenResponse {
        val user = userFinder.findByEmail(request.email)

        if (!CryptoUtil.verifyPassword(request.password, user.password)) {
            throw BusinessException(AuthErrorCode.NOT_MATCHED_PASSWORD)
        }

        return tokenHandler.generate(user)
    }

    suspend fun logout(token: String) {
        tokenHandler.deleteToken(token)
    }

    suspend fun sendSignUpVerificationEmail(email: String) {
        emailVerificationHandler.sendSignUpVerificationEmail(email, RandomNumberGenerator.generateRandomNumber(6))
    }

    suspend fun verifySignUpEmailCode(email: String, code: String): AuthResponse.SignUpVerifyEmailCodeResponse {
        return emailVerificationHandler.verifySignUpEmailCode(email, code)
    }

    @Transactional
    suspend fun signUp(request: AuthRequest.SignUpRequest): TokenResponse {
        emailVerificationHandler.checkVerifiedEmail(request.email)

        if (userExistChecker.isExistByEmail(request.email)) {
            throw BusinessException(AuthErrorCode.ALREADY_REGISTERED_USER)
        }

        val hashedPassword = CryptoUtil.hashPassword(request.password)

        val user = userInserter.insert(
            request.email,
            hashedPassword,
            Role.USER,
            request.nickname,
            request.career,
            request.position
        )

        emailVerificationHandler.deleteVerifiedSession(request.email)

        return tokenHandler.generate(user)
    }
}