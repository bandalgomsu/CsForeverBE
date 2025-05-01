package com.csforever.app.question.dto

class QuestionCommandRequest {
    data class QuestionSubmitRequest(
        val questionId: Long,
        val answer: String
    )
}