package com.csforever.app.auth.model

enum class EmailVerificationPrefix {
    SIGN_UP,
    SIGN_UP_VERIFIED;

    fun createKey(key: String): String {
        return "${this.name}:$key"
    }
}