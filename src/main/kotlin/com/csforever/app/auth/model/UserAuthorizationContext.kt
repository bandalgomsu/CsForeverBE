package com.csforever.app.auth.model

import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User

data class UserAuthorizationContext(
    val user: User? = null,
    val role: Role = Role.GUEST,
    val token: String? = null,
) {
}