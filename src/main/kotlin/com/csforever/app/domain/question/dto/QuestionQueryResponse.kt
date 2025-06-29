package com.csforever.app.domain.question.dto

class QuestionQueryResponse {
    data class QuestionInfo(
        val questionId: Long,
        val question: String,
        val bestAnswer: String,
        val isSolution: Boolean = false
    )
}