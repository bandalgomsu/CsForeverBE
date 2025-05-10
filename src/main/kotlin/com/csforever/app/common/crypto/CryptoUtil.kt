package com.csforever.app.common.crypto

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class CryptoUtil {
    companion object {
        fun hashPassword(password: String): String {
            return BCryptPasswordEncoder().encode(password)
        }

        fun verifyPassword(rawPassword: String, encodedPassword: String): Boolean {
            return BCryptPasswordEncoder().matches(rawPassword, encodedPassword)
        }
    }
}