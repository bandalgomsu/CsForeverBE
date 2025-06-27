package com.csforever.app.domain.user.submission.implement

import com.csforever.app.domain.user.submission.dao.SubmissionDao
import org.springframework.stereotype.Component

@Component
class SubmissionCounter(
    private val submissionDao: SubmissionDao
) {

    suspend fun countByUserIdAndIsCorrect(userId: Long, isCorrect: Boolean): Long {
        return submissionDao.countDistinctQuestionIdByUserIdAndCorrect(
            userId = userId,
            isCorrect = isCorrect
        )
    }

    suspend fun countAllByUserId(userId: Long): Long {
        return submissionDao.countAllByUserId(userId)
    }

}