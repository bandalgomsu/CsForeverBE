package com.csforever.app.question.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class QuestionErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    QUESTION_NOT_FOUND("Q01", "BOOK_NOT_FOUND", HttpStatus.NOT_FOUND.value()),
    ANSWER_LENGTH_TOO_SHORT("Q02", "ANSWER_LENGTH_TOO_SHORT", HttpStatus.BAD_REQUEST.value()),
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