package com.csforever.app.auth.model

import com.csforever.app.user.model.Role

data class UserAuthorizationContext(
    val userId: Long? = null,
    val role: Role = Role.GUEST
) {
}