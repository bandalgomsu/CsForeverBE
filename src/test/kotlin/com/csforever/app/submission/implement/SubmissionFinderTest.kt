package com.csforever.app.submission.implement

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.user.submission.dao.SubmissionDao
import com.csforever.app.domain.user.submission.implement.SubmissionFinder
import com.csforever.app.submission.SubmissionTestUtil
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SubmissionFinderTest {
    private val submissionDao: SubmissionDao = mockk<SubmissionDao>()

    private val submissionFinder = SubmissionFinder(submissionDao)

    @Test
    @DisplayName("UserId , IsCorrect 기반 문제 제출 이력 페이지 조회")
    fun `Test findPageByUserIdAndIsCorrect()`() = runTest {
        val userId = 1L
        val isCorrect = true
        val size = 5
        val page = 1

        val submissionId = 1L
        val questionId = 1L

        val submission = SubmissionTestUtil.createTestSubmission(
            submissionId = submissionId,
            userId = userId,
            questionId = questionId
        )
        val submissionPage = PageResponse(
            results = listOf(submission),
            totalElements = 1,
            totalPages = 1,
            currentPage = page,
            pageSize = size
        )

        coEvery { submissionFinder.findPageByUserIdAndIsCorrect(userId, isCorrect, size, page) } returns submissionPage

        val response = submissionFinder.findPageByUserIdAndIsCorrect(
            userId = userId,
            isCorrect = isCorrect,
            size = size,
            page = page
        )

        assert(response.results.size == 1)
        assert(response.results[0].id == submissionId)
        assert(response.results[0].userId == userId)
        assert(response.results[0].questionId == questionId)
        assert(response.totalElements == 1L)
        assert(response.totalPages == 1L)
        assert(response.currentPage == page)
        assert(response.pageSize == size)
    }

    @Test
    @DisplayName("UserId , IsCorrect , questionIds 기반 문제 제출 이력 페이지 조회")
    fun `Test findPageByUserIdAndIsCorrectAndQuestionIds()`() = runTest {
        val userId = 1L
        val isCorrect = true
        val size = 5
        val page = 1

        val submissionId = 1L
        val questionId = 1L

        val submission = SubmissionTestUtil.createTestSubmission(
            submissionId = submissionId,
            userId = userId,
            questionId = questionId
        )
        val submissionPage = PageResponse(
            results = listOf(submission),
            totalElements = 1,
            totalPages = 1,
            currentPage = page,
            pageSize = size
        )

        coEvery {
            submissionFinder.findPageByUserIdAndIsCorrectAndQuestionIds(
                userId,
                isCorrect,
                listOf(questionId),
                size,
                page
            )
        } returns submissionPage

        val response = submissionFinder.findPageByUserIdAndIsCorrectAndQuestionIds(
            userId = userId,
            isCorrect = isCorrect,
            size = size,
            page = page,
            questionIds = listOf(questionId)
        )

        assert(response.results.size == 1)
        assert(response.results[0].id == submissionId)
        assert(response.results[0].userId == userId)
        assert(response.results[0].questionId == questionId)
        assert(response.totalElements == 1L)
        assert(response.totalPages == 1L)
        assert(response.currentPage == page)
        assert(response.pageSize == size)
    }
}