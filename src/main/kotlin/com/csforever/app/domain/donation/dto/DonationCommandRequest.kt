package com.csforever.app.domain.donation.dto

import com.csforever.app.domain.question.model.QuestionTag

class DonationCommandRequest {
    data class DonationCreateRequest(
        val question: String,
        val bestAnswer: String,
        val tag: QuestionTag,
    )

    data class DonationEditRequest(
        val question: String,
        val bestAnswer: String,
        val tag: QuestionTag,
    )
}