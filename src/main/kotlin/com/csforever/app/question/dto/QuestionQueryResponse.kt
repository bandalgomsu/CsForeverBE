package com.csforever.app.question.dto

class QuestionQueryResponse {
    data class QuestionInfo(
        val questionId: Long,
        val question: String,
    )
}