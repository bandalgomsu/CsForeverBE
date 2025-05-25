package com.csforever.app.common.crypto

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.MessageDigest

class CryptoUtil {
    companion object {
        fun hashPassword(password: String): String {
            return BCryptPasswordEncoder().encode(password)
        }

        fun verifyPassword(rawPassword: String, encodedPassword: String): Boolean {
            return BCryptPasswordEncoder().matches(rawPassword, encodedPassword)
        }

        fun toSha256(input: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}