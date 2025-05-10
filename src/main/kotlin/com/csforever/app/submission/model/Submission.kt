package com.csforever.app.submission.model

import java.time.LocalDateTime

class Submission(
    val id: Long? = null,
    val userId: Long,
    val questionId: Long,
    val answer: String,
    val isCorrect: Boolean,
    val feedback: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}