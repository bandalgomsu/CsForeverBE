package com.csforever.app.user.dto

import java.time.LocalDateTime

class UserProfileResponse {
    data class UserProfile(
        val id: Long,
        val email: String,
        val nickname: String,
        val career: Int,
        val position: String,
        val correctSubmissionCount: Long,
        val ranking: Long?
    )

    data class UserProfileSubmission(
        val submissionId: Long,
        val questionId: Long,
        val userId: Long,
        val question: String,
        val tag: String,
        val answer: String,
        val feedback: String,
        val isCorrect: Boolean,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null,
    )
}