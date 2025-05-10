package com.csforever.app.user.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class UserErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    USER_NOT_FOUND("U01", "USER_NOT_FOUND", HttpStatus.NOT_FOUND.value()),
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