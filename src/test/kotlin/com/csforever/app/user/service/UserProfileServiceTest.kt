package com.csforever.app.user.service

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.question.QuestionTestUtil
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.ranking.RankingTestUtil
import com.csforever.app.ranking.exception.RankingErrorCode
import com.csforever.app.ranking.implement.RankingFinder
import com.csforever.app.submission.SubmissionTestUtil
import com.csforever.app.submission.implement.SubmissionCounter
import com.csforever.app.submission.implement.SubmissionFinder
import com.csforever.app.user.UserTestUtil
import com.csforever.app.user.dto.UserProfileRequest
import com.csforever.app.user.implement.UserFinder
import com.csforever.app.user.implement.UserUpdater
import com.csforever.app.user.model.Position
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

class UserProfileServiceTest {
    private val submissionCounter: SubmissionCounter = mockk(relaxed = true)
    private val submissionFinder: SubmissionFinder = mockk(relaxed = true)
    private val questionFinder: QuestionFinder = mockk(relaxed = true)
    private val rankingFinder: RankingFinder = mockk(relaxed = true)
    private val userFinder: UserFinder = mockk(relaxed = true)
    private val userUpdater: UserUpdater = mockk(relaxed = true)

    private val userProfileService = UserProfileService(
        submissionCounter = submissionCounter,
        submissionFinder = submissionFinder,
        questionFinder = questionFinder,
        rankingFinder = rankingFinder,
        userFinder = userFinder,
        userUpdater = userUpdater
    )

    @Test
    @DisplayName("유저 프로필 조회")
    fun `Test getUserProfile()`() = runTest {
        val userId = 1L
        val user = UserTestUtil.createTestUser(userId)
        val userProfile = UserTestUtil.createTestUserProfile(user)

        val rankingId = 1L
        val ranking = RankingTestUtil.createTestRanking(
            rankingId = rankingId,
            userId = userId,
            correctSubmissionCount = userProfile.correctSubmissionCount
        )

        coEvery { submissionCounter.countByUserIdAndIsCorrect(userId, true) } returns userProfile.correctSubmissionCount
        coEvery { submissionCounter.countAllByUserId(userId) } returns userProfile.submissionCount
        coEvery { userFinder.findById(userId) } returns user
        coEvery { rankingFinder.findByUserId(userId) } returns ranking

        val response = userProfileService.getUserProfile(userId)

        assert(response.id == userId)
        assert(response.nickname == user.nickname)
        assert(response.career == user.career)
        assert(response.position == user.position.krName)
        assert(response.correctSubmissionCount == userProfile.correctSubmissionCount)
        assert(response.submissionCount == userProfile.submissionCount)
        assert(response.correctPercent == userProfile.correctPercent)
        assert(response.ranking == ranking.ranking)
        assert(response.email == user.email)
    }

    @Test
    @DisplayName("유저 프로필 조회 (랭킹 없음)")
    fun `Test getUserProfile() Ranking Not Found`() = runTest {
        val userId = 1L
        val user = UserTestUtil.createTestUser(userId)
        val userProfile = UserTestUtil.createTestUserProfile(user)

        coEvery { submissionCounter.countByUserIdAndIsCorrect(userId, true) } returns userProfile.correctSubmissionCount
        coEvery { submissionCounter.countAllByUserId(userId) } returns userProfile.submissionCount
        coEvery { userFinder.findById(userId) } returns user
        coEvery { rankingFinder.findByUserId(userId) } throws BusinessException(RankingErrorCode.RANKING_NOT_FOUND)

        val response = userProfileService.getUserProfile(userId)

        assertNull(response.ranking)
    }

    @Test
    @DisplayName("유저 프로필 업데이트")
    fun `Test updateUserProfile`() = runTest {
        val userId = 1L

        val newNickname = "new_nickname"
        val newCareer = 2
        val newPosition = Position.FRONTEND

        val request = UserProfileRequest.UserProfileUpdateRequest(
            nickname = newNickname,
            career = newCareer,
            position = newPosition
        )

        coEvery {
            userUpdater.updateUserProfile(
                userId,
                request.nickname,
                request.career,
                request.position
            )
        } returns Unit

        userProfileService.updateUserProfile(userId, request)

        coVerify(exactly = 1) {
            userUpdater.updateUserProfile(
                userId,
                newNickname,
                newCareer,
                newPosition
            )
        }
    }

    @Test
    @DisplayName("유저 프로필 제출 이력 페이지 조회")
    fun `Test findUserProfileSubmissionPage`() = runTest {
        val userId = 1L
        val user = UserTestUtil.createTestUser(userId)
        val isCorrect = true
        val size = 5
        val page = 1

        val submissionId = 1L
        val submission = SubmissionTestUtil.createTestSubmission(submissionId)

        val submissionPage = PageResponse(
            results = listOf(submission),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        val question = QuestionTestUtil.createTestQuestion(submission.questionId)

        coEvery { submissionFinder.findPageByUserIdAndIsCorrect(userId, isCorrect, size, page) } returns submissionPage
        coEvery { questionFinder.findAllByIdIn(listOf(submission.questionId)) } returns listOf(question)

        val response = userProfileService.findUserProfileSubmissionPage(user, isCorrect, size, page)

        assert(response.results.size == 1)
        assert(response.results[0].submissionId == submissionId)
        assert(response.results[0].questionId == submission.questionId)
        assert(response.results[0].userId == userId)
        assert(response.results[0].question == question.question)
        assert(response.results[0].tag == question.tag.displayName)
        assert(response.results[0].answer == submission.answer)
        assert(response.results[0].feedback == submission.feedback)
        assert(response.results[0].isCorrect == submission.isCorrect)
        assert(response.totalPages == submissionPage.totalPages)
        assert(response.totalElements == submissionPage.totalElements)
        assert(response.currentPage == submissionPage.currentPage)
        assert(response.pageSize == submissionPage.pageSize)
    }

    @Test
    @DisplayName("태그 기반 유저 프로필 제출 이력 페이지 조회")
    fun `Test findUserProfileSubmissionPageByTag`() = runTest {
        val userId = 1L
        val user = UserTestUtil.createTestUser(userId)
        val isCorrect = true
        val size = 5
        val page = 1

        val submissionId = 1L
        val submission = SubmissionTestUtil.createTestSubmission(submissionId)

        val submissionPage = PageResponse(
            results = listOf(submission),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        val question = QuestionTestUtil.createTestQuestion(submission.questionId)

        coEvery { questionFinder.findIdsByTag(question.tag) } returns listOf(question.id!!)
        coEvery {
            submissionFinder.findPageByUserIdAndIsCorrectAndQuestionIds(
                userId,
                isCorrect,
                listOf(question.id!!),
                size,
                page
            )
        } returns submissionPage
        coEvery { questionFinder.findAllByIdIn(listOf(submission.questionId)) } returns listOf(question)

        val response = userProfileService.findUserProfileSubmissionPageByTag(user, isCorrect, size, page, question.tag)

        assert(response.results.size == 1)
        assert(response.results[0].submissionId == submissionId)
        assert(response.results[0].questionId == submission.questionId)
        assert(response.results[0].userId == userId)
        assert(response.results[0].question == question.question)
        assert(response.results[0].tag == question.tag.displayName)
        assert(response.results[0].answer == submission.answer)
        assert(response.results[0].feedback == submission.feedback)
        assert(response.results[0].isCorrect == submission.isCorrect)
        assert(response.totalPages == submissionPage.totalPages)
        assert(response.totalElements == submissionPage.totalElements)
        assert(response.currentPage == submissionPage.currentPage)
        assert(response.pageSize == submissionPage.pageSize)
    }
}