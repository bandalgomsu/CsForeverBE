package com.csforever.app.domain.donation.model

import com.csforever.app.domain.question.model.QuestionTag
import java.time.LocalDateTime

data class Donation(
    val id: Long? = null,
    val question: String,
    val bestAnswer: String,
    val tag: QuestionTag,
    val userId: Long,
    val questionId: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {

    fun isConfirmed(): Boolean {
        return questionId != null
    }

    fun confirm(questionId: Long): Donation {
        return this.copy(questionId = questionId)
    }
}