package com.csforever.app.domain.question.service

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.common.scope.CustomScope
import com.csforever.app.domain.question.dto.QuestionQueryResponse
import com.csforever.app.domain.question.implement.QuestionFinder
import com.csforever.app.domain.question.implement.QuestionPaginationFinder
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.app.domain.user.profile.model.User
import com.csforever.app.domain.user.submission.implement.SubmissionExistChecker
import com.csforever.app.domain.user.submission.implement.SubmissionFinder
import com.csforever.app.domain.user.submission.implement.SubmissionInserter
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class QuestionQueryService(
    private val questionFinder: QuestionFinder,
    private val questionPaginationFinder: QuestionPaginationFinder,

    private val submissionFinder: SubmissionFinder,
    private val submissionInserter: SubmissionInserter,
    private val submissionExistChecker: SubmissionExistChecker
) {

    suspend fun findRandomByTags(tags: List<QuestionTag>, userId: Long): QuestionQueryResponse.QuestionInfo {
        val question = questionFinder.findRandomByTags(tags)
        val isSolution = submissionExistChecker.existByUserIdAndQuestionId(
            userId = userId,
            questionId = question.id!!,
            isCorrect = true
        )

        return question.toInfo(isSolution)
    }

    suspend fun findBestAnswerById(questionId: Long, user: User?): QuestionQueryResponse.QuestionInfo {
        val question = questionFinder.findById(questionId)

        CustomScope.fireAndForget.launch {
            submissionInserter.insert(
                userId = user?.id ?: 0, // 게스트의 user_id = 0 이라 하자
                questionId = questionId,
                answer = "", // 답안 보기이므로 빈 문자열
                isCorrect = false, // 답안 보기이므로 정답 여부는 false
                feedback = "" // 답안 보기이므로 빈 문자열
            )
        }

        return question.toInfo()
    }

    suspend fun findPage(
        tag: QuestionTag,
        page: Int,
        size: Int,
        userId: Long
    ): PageResponse<QuestionQueryResponse.QuestionInfo> {
        val questionPage = questionPaginationFinder.findPageByTag(
            tag = tag,
            size = size,
            page = page
        )

        val submissions = submissionFinder.findAllByUserIdAndQuestionIdIn(
            userId = userId,
            questionIds = questionPage.results.map { it.id!! },
            isCorrect = true
        ).associateBy { it.questionId }

        return PageResponse(
            results = questionPage.results.map { question ->
                val isSolution = submissions.containsKey(question.id!!)
                question.toInfo(isSolution)
            },
            totalPages = questionPage.totalPages,
            totalElements = questionPage.totalElements,
            currentPage = questionPage.currentPage,
            pageSize = questionPage.pageSize
        )
    }

    suspend fun findById(questionId: Long, userId: Long): QuestionQueryResponse.QuestionInfo = coroutineScope {

        val questionDeffer = async {
            questionFinder.findById(questionId)
        }

        val isSolutionDeffer = async {
            submissionExistChecker.existByUserIdAndQuestionId(
                userId = userId,
                questionId = questionId,
                isCorrect = true
            )
        }

        val question = questionDeffer.await()
        val isSolution = isSolutionDeffer.await()

        return@coroutineScope question.toInfo(isSolution)
    }
}