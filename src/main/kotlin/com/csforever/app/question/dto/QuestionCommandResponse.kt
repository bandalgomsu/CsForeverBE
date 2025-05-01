package com.csforever.app.question.dto

class QuestionCommandResponse {
    data class QuestionSubmitResponse(
        val isAnswer: Boolean,
        val feedback: String
    )
}