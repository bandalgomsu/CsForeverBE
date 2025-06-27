package com.csforever.app.domain.user.profile.model

import java.time.LocalDateTime

class User(
    val id: Long? = null,
    val email: String,
    val password: String,
    val role: Role,
    val nickname: String,
    val career: Int,
    val position: Position,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}