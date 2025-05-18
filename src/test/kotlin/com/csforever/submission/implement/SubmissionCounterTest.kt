package com.csforever.submission.implement

import com.csforever.app.submission.dao.SubmissionDao
import com.csforever.app.submission.implement.SubmissionCounter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SubmissionCounterTest {

    private val submissionDao: SubmissionDao = mockk(relaxed = true)

    private val submissionCounter: SubmissionCounter = SubmissionCounter(submissionDao)


    @Test
    fun countByUserIdAndIsCorrectTest() = runBlocking {
        val userId = 1L
        val isCorrect = true

        coEvery { submissionDao.countDistinctQuestionIdByUserIdAndCorrect(userId, isCorrect) } returns 5L

        val response = submissionCounter.countByUserIdAndIsCorrect(userId, isCorrect)

        assertEquals(5, response)
    }
}