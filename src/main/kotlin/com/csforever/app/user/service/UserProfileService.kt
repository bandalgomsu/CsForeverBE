package com.csforever.app.user.service

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.submission.implement.SubmissionCounter
import com.csforever.app.submission.implement.SubmissionFinder
import com.csforever.app.user.dto.UserProfileResponse
import com.csforever.app.user.model.User
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val submissionCounter: SubmissionCounter,
    private val submissionFinder: SubmissionFinder,
    private val questionFinder: QuestionFinder
) {

    suspend fun getUserProfile(user: User): UserProfileResponse.UserProfile {
        val correctSubmissionCount = submissionCounter.countByUserIdAndIsCorrect(user.id!!, true)

        return UserProfileResponse.UserProfile(
            id = user.id,
            email = user.email,
            nickname = user.nickname,
            career = user.career,
            position = user.position.krName,
            correctSubmissionCount = correctSubmissionCount
        )
    }

    suspend fun findUserProfileSubmissionPage(
        user: User,
        isCorrect: Boolean = true,
        size: Int = 5,
        page: Int = 1
    ): PageResponse<UserProfileResponse.UserProfileSubmission> {
        val submissionPage = submissionFinder.findPageByUserIdAndIsCorrect(
            userId = user.id!!,
            isCorrect = isCorrect,
            size = size,
            page = page
        )

        val questionIds = submissionPage.results.map { it.questionId }
        val questions = questionFinder.findAllByIdIn(questionIds)

        val questionMap = questions.associateBy { it.id }

        val results = submissionPage.results
            .filter { it.questionId in questionMap.keys }
            .map { submission ->
                val question = questionMap[submission.questionId] ?: throw BusinessException(
                    "Question Not Found question_id : ${submission.questionId} , question_ids : ${questionIds} , user_id : ${user.id} ",
                    QuestionErrorCode.QUESTION_NOT_FOUND
                )

                UserProfileResponse.UserProfileSubmission(
                    submissionId = submission.id!!,
                    questionId = question.id!!,
                    userId = submission.userId,
                    question = question.question,
                    tag = question.tag.displayName,
                    answer = submission.answer,
                    feedback = submission.feedback,
                    isCorrect = submission.isCorrect,
                    createdAt = submission.createdAt,
                    updatedAt = submission.updatedAt,
                )
            }

        return PageResponse(
            results = results,
            totalPages = submissionPage.totalPages,
            totalElements = submissionPage.totalElements,
            currentPage = submissionPage.currentPage,
            pageSize = submissionPage.pageSize,
        )
    }
}