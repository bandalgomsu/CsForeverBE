package com.csforever.app.auth.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    NOT_MATCHED_PASSWORD("A01", "NOT_MATCHED_PASSWORD", HttpStatus.UNAUTHORIZED.value()),
    NOT_LOGIN_USER("A02", "NOT_LOGIN_USER", HttpStatus.UNAUTHORIZED.value()),
    ALREADY_REGISTERED_USER("A03", "ALREADY_REGISTERED_USER", HttpStatus.BAD_REQUEST.value()),
    INVALID_VERIFICATION_CODE("A04", "INVALID_VERIFICATION_CODE", HttpStatus.BAD_REQUEST.value()),
    EXPIRED_VERIFICATION_CODE("A05", "EXPIRED_VERIFICATION_CODE", HttpStatus.BAD_REQUEST.value()),
    NOT_EXISTS_VERIFICATION_SESSION("A06", "NOT_EXISTS_VERIFICATION_SESSION", HttpStatus.BAD_REQUEST.value()),
    ;

    override fun getCodeValue(): String {
        return this.code
    }

    override fun getStatusValue(): Int {
        return this.status
    }

    override fun getMessageValue(): String {
        return this.message
    }
}