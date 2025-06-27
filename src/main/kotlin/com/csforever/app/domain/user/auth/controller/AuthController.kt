package com.csforever.app.domain.user.auth.controller

import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.dto.AuthRequest
import com.csforever.app.domain.user.auth.dto.AuthResponse
import com.csforever.app.domain.user.auth.dto.TokenResponse
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.domain.user.auth.service.AuthService
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

    @DeleteMapping("/logout")
    suspend fun logout(@AuthorizationContext userAuthorizationContext: UserAuthorizationContext): ResponseEntity<Unit> {
        authService.logout(userAuthorizationContext.token!!)

        return ResponseEntity.ok().build()
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