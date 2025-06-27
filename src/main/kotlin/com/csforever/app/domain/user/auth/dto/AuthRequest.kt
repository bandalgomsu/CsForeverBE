package com.csforever.app.domain.user.auth.dto

import com.csforever.app.domain.user.profile.model.Position

class AuthRequest {
    data class LoginRequest(
        val email: String,
        val password: String,
    )

    data class VerifySignUpEmailCodeRequest(
        val email: String,
        val code: String
    )

    data class SignUpRequest(
        val email: String,
        val password: String,
        val nickname: String,
        val career: Int,
        val position: Position,
    )
}