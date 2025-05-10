package com.csforever.app.question.dto

class QuestionCommandResponse {
    data class QuestionSubmitResponse(
        val isCorrect: Boolean,
        val feedback: String
    )
}