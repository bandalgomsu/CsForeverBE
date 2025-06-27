package com.csforever.app.domain.user.submission.implement

import com.csforever.app.domain.user.submission.dao.SubmissionDao
import org.springframework.stereotype.Component

@Component
class SubmissionExistChecker(
    private val submissionDao: SubmissionDao
) {

    suspend fun existByUserIdAndQuestionId(
        userId: Long,
        questionId: Long,
        isCorrect: Boolean
    ): Boolean {
        return submissionDao.existsByUserIdAndQuestionIdAndIsCorrect(userId, questionId, isCorrect)
    }
}