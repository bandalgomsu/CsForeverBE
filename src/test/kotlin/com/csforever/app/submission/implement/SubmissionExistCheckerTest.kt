package com.csforever.app.submission.implement

import com.csforever.app.domain.user.submission.dao.SubmissionDao
import com.csforever.app.domain.user.submission.implement.SubmissionExistChecker
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SubmissionExistCheckerTest {
    private val submissionDao: SubmissionDao = mockk<SubmissionDao>()

    private val submissionExistChecker = SubmissionExistChecker(submissionDao)

    @Test
    @DisplayName("UserId , QuestionId 기반 문제 제출 존재 여부 확인")
    fun `Test existByUserIdAndQuestionId`() = runTest {
        val userId = 1L
        val questionId = 1L
        val isCorrect = true

        coEvery { submissionDao.existsByUserIdAndQuestionIdAndIsCorrect(userId, questionId, isCorrect) } returns true

        val response = submissionExistChecker.existByUserIdAndQuestionId(userId, questionId, isCorrect)

        assertTrue(response)
    }
}