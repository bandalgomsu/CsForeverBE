package com.csforever.app.submission

import com.csforever.app.submission.model.Submission

class SubmissionTestUtil {
    companion object {
        fun createTestSubmission(submissionId: Long? = null, userId: Long = 1L, questionId: Long = 1L): Submission {
            return Submission(
                id = submissionId,
                userId = userId,
                questionId = questionId,
                answer = "test_answer",
                feedback = "test_feedback",
                isCorrect = true
            )
        }
    }
}