package com.csforever.app.question.dto

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.question.exception.QuestionErrorCode

class QuestionCommandRequest {
    data class QuestionSubmitRequest(
        val questionId: Long,
        val answer: String
    ) {
        fun validateAnswerLength() {
            if (answer.length < 10) {
                throw BusinessException(QuestionErrorCode.ANSWER_LENGTH_TOO_SHORT)
            } else if (answer.length > 300) {
                throw BusinessException(QuestionErrorCode.ANSWER_LENGTH_TOO_LONG)
            }
        }
    }
}