package com.csforever.app.auth.controller

import com.csforever.app.auth.dto.AuthRequest
import com.csforever.app.auth.dto.AuthResponse
import com.csforever.app.auth.dto.TokenResponse
import com.csforever.app.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    suspend fun login(@RequestBody request: AuthRequest.LoginRequest): ResponseEntity<TokenResponse> {
        val response = authService.login(request)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/mail/{email}")
    suspend fun sendSignUpVerificationEmail(@PathVariable email: String): ResponseEntity<Unit> {
        authService.sendSignUpVerificationEmail(email)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/mail/verify")
    suspend fun verifySignUpEmailCode(@RequestBody request: AuthRequest.VerifySignUpEmailCodeRequest): ResponseEntity<AuthResponse.SignUpVerifyEmailCodeResponse> {
        val response = authService.verifySignUpEmailCode(request.email, request.code)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/signUp")
    suspend fun signup(@RequestBody request: AuthRequest.SignUpRequest): ResponseEntity<TokenResponse> {
        val response = authService.signUp(request)

        return ResponseEntity.ok(response)
    }
}