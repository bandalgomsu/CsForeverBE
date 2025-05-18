package com.csforever.submission.implement

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.submission.dao.SubmissionDao
import com.csforever.app.submission.implement.SubmissionFinder
import com.csforever.app.submission.model.Submission
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SubmissionFinderTest {
    private val submissionDao: SubmissionDao = mockk(relaxed = true)

    private val submissionFinder = SubmissionFinder(submissionDao)


    @Test
    fun findPageByUserIdAndIsCorrect() = runBlocking {
        val submission = Submission(
            id = 1L,
            userId = 1L,
            questionId = 1L,
            answer = "answer",
            isCorrect = true,
            feedback = "feedback"
        )

        val pageResponse = PageResponse(
            results = listOf(
                submission
            ),
            totalPages = 1,
            totalElements = 1,
            currentPage = 1,
            pageSize = 1
        )

        coEvery {
            submissionDao.findPageByUserIdAndIsCorrect(
                submission.userId,
                submission.isCorrect,
                1,
                1
            )
        } returns pageResponse

        // When
        val response = submissionFinder.findPageByUserIdAndIsCorrect(
            submission.userId,
            submission.isCorrect,
            1,
            1
        )

        assertEquals(pageResponse, response)
    }

}