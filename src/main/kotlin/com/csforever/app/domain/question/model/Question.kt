package com.csforever.app.domain.question.model

import com.csforever.app.domain.question.dto.QuestionQueryResponse

class Question(
    val id: Long? = null,
    val question: String,
    val bestAnswer: String,
    val tag: QuestionTag,
) {
    fun toInfo(): QuestionQueryResponse.QuestionInfo {
        return QuestionQueryResponse.QuestionInfo(
            questionId = id!!,
            question = question,
            bestAnswer = bestAnswer,
        )
    }

    fun toInfo(isSolution: Boolean): QuestionQueryResponse.QuestionInfo {
        return QuestionQueryResponse.QuestionInfo(
            questionId = id!!,
            question = question,
            bestAnswer = bestAnswer,
            isSolution = isSolution
        )
    }
}