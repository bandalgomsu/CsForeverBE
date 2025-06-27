package com.csforever.app.domain.user.profile.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class UserErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    USER_NOT_FOUND("U01", "USER_NOT_FOUND", HttpStatus.NOT_FOUND.value()),
    USER_BLANK_NICKNAME("U02", "USER_BLANK_NICKNAME", HttpStatus.BAD_REQUEST.value()),
    USER_TOO_LONG_NICKNAME("U03", "USER_TOO_LONG_NICKNAME", HttpStatus.BAD_REQUEST.value()),
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