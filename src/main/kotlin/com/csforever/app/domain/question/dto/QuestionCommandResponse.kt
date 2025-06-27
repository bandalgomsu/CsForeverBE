package com.csforever.app.domain.question.dto

class QuestionCommandResponse {
    data class QuestionSubmitResponse(
        val isCorrect: Boolean,
        val feedback: String
    )
}