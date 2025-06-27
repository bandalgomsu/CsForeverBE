package com.csforever.app.domain.user.auth.service

import com.csforever.app.domain.user.auth.dto.AuthRequest
import com.csforever.app.domain.user.auth.dto.AuthResponse
import com.csforever.app.domain.user.auth.dto.TokenResponse
import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.domain.user.auth.implement.EmailVerificationHandler
import com.csforever.app.domain.user.auth.implement.TokenHandler
import com.csforever.app.common.crypto.CryptoUtil
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.mail.RandomNumberGenerator
import com.csforever.app.domain.user.profile.implement.UserExistChecker
import com.csforever.app.domain.user.profile.implement.UserFinder
import com.csforever.app.domain.user.profile.implement.UserInserter
import com.csforever.app.domain.user.profile.model.Role
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

    suspend fun sendSignUpVerificationEmail(
        email: String,
        code: String = RandomNumberGenerator.generateRandomNumber(6)
    ) {
        emailVerificationHandler.sendSignUpVerificationEmail(email, code)
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