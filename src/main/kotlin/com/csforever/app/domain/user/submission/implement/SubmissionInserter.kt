package com.csforever.app.domain.user.submission.implement

import com.csforever.app.domain.user.submission.dao.SubmissionDao
import com.csforever.app.domain.user.submission.model.Submission
import org.springframework.stereotype.Component

@Component
class SubmissionInserter(
    private val submissionDao: SubmissionDao
) {

    suspend fun insert(
        userId: Long,
        questionId: Long,
        answer: String,
        isCorrect: Boolean,
        feedback: String
    ): Submission {
        return submissionDao.insert(
            Submission(
                userId = userId,
                questionId = questionId,
                answer = answer,
                isCorrect = isCorrect,
                feedback = feedback,
            )
        )
    }
}