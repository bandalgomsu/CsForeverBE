package com.csforever.app.user.dto

import com.csforever.app.question.model.Question
import com.csforever.app.submission.model.Submission
import com.csforever.app.user.model.User
import java.time.LocalDateTime

class UserProfileResponse {
    data class UserProfile(
        val id: Long,
        val email: String,
        val nickname: String,
        val career: Int,
        val position: String,
        val correctSubmissionCount: Long,
        val submissionCount: Long,
        val correctPercent: String,
        val ranking: Long?
    ) {
        companion object {
            fun create(user: User, ranking: Long?, correctSubmissionCount: Long, submissionCount: Long): UserProfile {
                val correctPercent = if (submissionCount > 0) {
                    ((correctSubmissionCount.toFloat() / submissionCount * 100).coerceAtMost(100f))
                } else {
                    0f
                }

                return UserProfile(
                    id = user.id!!,
                    email = user.email,
                    nickname = user.nickname,
                    career = user.career,
                    position = user.position.krName,
                    correctSubmissionCount = correctSubmissionCount,
                    submissionCount = submissionCount,
                    correctPercent = "%.2f".format(correctPercent),
                    ranking = ranking
                )
            }
        }
    }

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
    ) {
        companion object {
            fun create(
                submission: Submission,
                question: Question
            ): UserProfileSubmission {
                return UserProfileSubmission(
                    submissionId = submission.id!!,
                    questionId = question.id!!,
                    userId = submission.userId,
                    question = question.question,
                    tag = question.tag.displayName,
                    answer = submission.answer,
                    feedback = submission.feedback,
                    isCorrect = submission.isCorrect,
                    createdAt = submission.createdAt,
                    updatedAt = submission.updatedAt,
                )
            }
        }
    }
}