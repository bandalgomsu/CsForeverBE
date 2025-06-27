package com.csforever.app.domain.user.auth.implement

import com.csforever.app.domain.user.auth.dto.TokenResponse
import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.domain.user.profile.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenHandler(
    private val redisClient: RedisClient,
    @Value("\${auth.token.expiration}") private val expireSeconds: Long,
) {
    suspend fun generate(user: User): TokenResponse {
        val token = UUID.randomUUID().toString()

        redisClient.setData(
            "session:$token",
            UserAuthorizationContext(user = user, role = user.role, token = token),
            expireSeconds
        )

        return TokenResponse(token)
    }

    suspend fun validateToken(token: String): UserAuthorizationContext {
        val authorization = redisClient.getData("session:$token", UserAuthorizationContext::class)
            ?: throw BusinessException(AuthErrorCode.NOT_LOGIN_USER)

        redisClient.setData("session:$token", authorization, expireSeconds)

        return authorization
    }

    suspend fun extractToken(token: String): UserAuthorizationContext {
        val authorization =
            redisClient.getData("session:$token", UserAuthorizationContext::class) ?: return UserAuthorizationContext()

        redisClient.setData("session:$token", authorization, expireSeconds)

        return authorization
    }

    suspend fun deleteToken(token: String) {
        redisClient.deleteData("session:$token")
    }
}