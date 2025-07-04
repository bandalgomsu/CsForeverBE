package com.csforever.app.domain.user.auth.dto

class AuthResponse {

    data class SignUpVerifyEmailCodeResponse(
        val email: String,
        val verificationCode: String,
        val isVerified: Boolean,
    )
}