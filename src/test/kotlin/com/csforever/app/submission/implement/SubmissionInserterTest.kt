package com.csforever.app.submission.implement

import com.csforever.app.submission.SubmissionTestUtil
import com.csforever.app.submission.dao.SubmissionDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SubmissionInserterTest {
    private val submissionDao: SubmissionDao = mockk(relaxed = true)

    private val submissionInserter = SubmissionInserter(submissionDao)

    @Test
    @DisplayName("문제 제출 이력 저장")
    fun `Test insert()`() = runTest {
        val submission = SubmissionTestUtil.createTestSubmission(userId = 1L)

        coEvery { submissionDao.insert(any()) } returns submission

        val response = submissionInserter.insert(
            userId = submission.userId,
            questionId = submission.questionId,
            answer = submission.answer,
            isCorrect = submission.isCorrect,
            feedback = submission.feedback
        )
        
        assert(response.userId == submission.userId)
        assert(response.questionId == submission.questionId)
        assert(response.answer == submission.answer)
        assert(response.isCorrect == submission.isCorrect)
        assert(response.feedback == submission.feedback)
    }
}