package com.csforever.app.question.model

import com.csforever.app.question.dto.QuestionQueryResponse

class Question(
    val id: Long? = null,
    val question: String,
    val bestAnswer: String,
    val tag: String,
) {
    fun toInfo(): QuestionQueryResponse.QuestionInfo {
        return QuestionQueryResponse.QuestionInfo(
            questionId = id!!,
            question = question,
        )
    }
}