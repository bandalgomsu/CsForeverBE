package com.csforever.app.common.llm

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class LLMErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    TOO_MANY_REQUEST("L01", "TOO_MANY_REQUEST", HttpStatus.TOO_MANY_REQUESTS.value()),
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