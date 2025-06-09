package com.csforever.app.submission.implement

import com.csforever.app.submission.dao.SubmissionDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SubmissionCounterTest {
    private val submissionDao: SubmissionDao = mockk<SubmissionDao>()

    private val submissionCounter = SubmissionCounter(submissionDao)

    @Test
    @DisplayName("UserId , 정답 여부 기반 문제 제출 수 카운트")
    fun `Test countByUserIdAndIsCorrect()`() = runTest {
        val userId = 1L
        val isCorrect = true
        val submissionCount = 5L

        coEvery { submissionDao.countDistinctQuestionIdByUserIdAndCorrect(userId, isCorrect) } returns submissionCount

        val response = submissionCounter.countByUserIdAndIsCorrect(userId, isCorrect)

        assert(response == submissionCount)
    }

    @Test
    @DisplayName("UserId 기반 문제 제출 수 카운트")
    fun `Test countByUserId()`() = runTest {
        val userId = 1L
        val submissionCount = 5L

        coEvery { submissionDao.countAllByUserId(userId) } returns submissionCount

        val response = submissionCounter.countAllByUserId(userId)

        assert(response == submissionCount)
    }
}