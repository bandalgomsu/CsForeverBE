package com.csforever.app.domain.user.auth.model

import com.csforever.app.domain.user.profile.model.Role
import com.csforever.app.domain.user.profile.model.User

data class UserAuthorizationContext(
    val user: User? = null,
    val role: Role = Role.GUEST,
    val token: String? = null,
) {
}