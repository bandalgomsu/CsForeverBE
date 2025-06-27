package com.csforever.app.domain.term.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class TermErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    INVALID_TERM("T01", "INVALID_TERM", HttpStatus.BAD_REQUEST.value()),
    INVALID_TERM_INPUT("T02", "INVALID_TERM_INPUT", HttpStatus.BAD_REQUEST.value()),
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