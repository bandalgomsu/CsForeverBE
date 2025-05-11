package com.csforever.app.submission.implement

import com.csforever.app.submission.dao.SubmissionDao
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

}