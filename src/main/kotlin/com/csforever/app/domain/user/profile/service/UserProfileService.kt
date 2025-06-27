package com.csforever.app.domain.user.profile.service

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.question.exception.QuestionErrorCode
import com.csforever.app.domain.question.implement.QuestionFinder
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.app.domain.ranking.exception.RankingErrorCode
import com.csforever.app.domain.ranking.implement.RankingFinder
import com.csforever.app.domain.user.profile.dto.UserProfileRequest
import com.csforever.app.domain.user.profile.dto.UserProfileResponse
import com.csforever.app.domain.user.profile.implement.UserFinder
import com.csforever.app.domain.user.profile.implement.UserUpdater
import com.csforever.app.domain.user.profile.model.User
import com.csforever.app.domain.user.submission.implement.SubmissionCounter
import com.csforever.app.domain.user.submission.implement.SubmissionFinder
import com.csforever.app.domain.user.submission.model.Submission
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val submissionCounter: SubmissionCounter,
    private val submissionFinder: SubmissionFinder,
    private val questionFinder: QuestionFinder,
    private val rankingFinder: RankingFinder,
    private val userFinder: UserFinder,
    private val userUpdater: UserUpdater
) {

    suspend fun getUserProfile(userId: Long): UserProfileResponse.UserProfile {
        val correctSubmissionCount = submissionCounter.countByUserIdAndIsCorrect(userId, true)
        val submissionCount = submissionCounter.countAllByUserId(userId)
        val user = userFinder.findById(userId)

        val ranking = try {
            rankingFinder.findByUserId(userId).ranking
        } catch (e: BusinessException) {
            when (e.errorCode) {
                RankingErrorCode.RANKING_NOT_FOUND -> null
                else -> throw e
            }
        }

        return UserProfileResponse.UserProfile.create(
            user = user,
            correctSubmissionCount = correctSubmissionCount,
            submissionCount = submissionCount,
            ranking = ranking
        )
    }

    suspend fun updateUserProfile(
        userId: Long,
        request: UserProfileRequest.UserProfileUpdateRequest
    ) {
        userUpdater.updateUserProfile(
            userId = userId,
            nickname = request.nickname,
            career = request.career,
            position = request.position
        )
    }

    suspend fun findUserProfileSubmissionPage(
        user: User,
        isCorrect: Boolean = true,
        size: Int = 5,
        page: Int = 1,
    ): PageResponse<UserProfileResponse.UserProfileSubmission> {
        val submissionPage = submissionFinder.findPageByUserIdAndIsCorrect(
            userId = user.id!!,
            isCorrect = isCorrect,
            size = size,
            page = page
        )

        val results = createUserProfileSubmissions(submissionPage, user.id)

        return PageResponse(
            results = results,
            totalPages = submissionPage.totalPages,
            totalElements = submissionPage.totalElements,
            currentPage = submissionPage.currentPage,
            pageSize = submissionPage.pageSize,
        )
    }

    suspend fun findUserProfileSubmissionPageByTag(
        user: User,
        isCorrect: Boolean = true,
        size: Int = 5,
        page: Int = 1,
        tag: QuestionTag
    ): PageResponse<UserProfileResponse.UserProfileSubmission> {
        val questionIds = questionFinder.findIdsByTag(
            tag = tag
        )

        val submissionPage = submissionFinder.findPageByUserIdAndIsCorrectAndQuestionIds(
            userId = user.id!!,
            isCorrect = isCorrect,
            questionIds = questionIds,
            size = size,
            page = page
        )

        val results = createUserProfileSubmissions(submissionPage, user.id)

        return PageResponse(
            results = results,
            totalPages = submissionPage.totalPages,
            totalElements = submissionPage.totalElements,
            currentPage = submissionPage.currentPage,
            pageSize = submissionPage.pageSize,
        )
    }

    private suspend fun createUserProfileSubmissions(
        submissionPage: PageResponse<Submission>,
        userId: Long
    ): List<UserProfileResponse.UserProfileSubmission> {
        val questionIdsInSubmission = submissionPage.results.map { it.questionId }
        val questions = questionFinder.findAllByIdIn(questionIdsInSubmission)

        val questionMap = questions.associateBy { it.id }

        return submissionPage.results
            .filter { it.questionId in questionMap.keys }
            .map { submission ->
                val question = questionMap[submission.questionId] ?: throw BusinessException(
                    "Question Not Found question_id : ${submission.questionId} , question_ids : ${questionIdsInSubmission} , user_id : ${userId} ",
                    QuestionErrorCode.QUESTION_NOT_FOUND
                )

                UserProfileResponse.UserProfileSubmission.create(
                    submission = submission,
                    question = question
                )
            }
    }
}