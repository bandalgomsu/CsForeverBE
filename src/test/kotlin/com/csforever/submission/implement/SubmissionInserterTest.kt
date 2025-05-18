package com.csforever.submission.implement

import com.csforever.app.submission.dao.SubmissionDao
import com.csforever.app.submission.implement.SubmissionInserter
import com.csforever.app.submission.model.Submission
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class SubmissionInserterTest {
    private val submissionDao: SubmissionDao = mockk(relaxed = true)

    private val submissionInserter = SubmissionInserter(submissionDao)


    @Test
    fun insertTest() = runBlocking {
        val submission = Submission(
            userId = 1L,
            questionId = 1L,
            answer = "test_answer",
            isCorrect = true,
            feedback = "test_feedback"
        )

        val savedSubmission = Submission(
            userId = 1L,
            questionId = 1L,
            answer = "test_answer",
            isCorrect = true,
            feedback = "test_feedback"
        )

        coEvery { submissionDao.insert(any()) } returns savedSubmission

        val response = submissionInserter.insert(
            userId = submission.userId,
            questionId = submission.questionId,
            answer = submission.answer,
            isCorrect = submission.isCorrect,
            feedback = submission.feedback
        )

        assert(response == savedSubmission)
    }
}