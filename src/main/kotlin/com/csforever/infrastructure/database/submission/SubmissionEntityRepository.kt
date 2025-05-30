package com.csforever.infrastructure.database.submission

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.submission.dao.SubmissionDao
import com.csforever.app.submission.model.Submission
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SubmissionEntityRepository(
    private val submissionCoroutineRepository: SubmissionCoroutineRepository
) : SubmissionDao {


    override suspend fun insert(submission: Submission): Submission {
        return submissionCoroutineRepository.save(
            SubmissionEntity(
                userId = submission.userId,
                questionId = submission.questionId,
                answer = submission.answer,
                isCorrect = submission.isCorrect,
                feedback = submission.feedback,
            )
        ).toModel()
    }

    override suspend fun countDistinctQuestionIdByUserIdAndCorrect(userId: Long, isCorrect: Boolean): Long {
        return submissionCoroutineRepository.countDistinctQuestionIdByUserIdAndCorrect(
            userId = userId,
            isCorrect = isCorrect
        )
    }

    @Transactional(readOnly = true)
    override suspend fun findPageByUserIdAndIsCorrect(
        userId: Long,
        isCorrect: Boolean,
        size: Int,
        page: Int
    ): PageResponse<Submission> = coroutineScope {
        val safePage = maxOf(page, 1)
        val offset = (safePage - 1) * size

        val submissionsDeferred = async {
            submissionCoroutineRepository.findPageByUserIdAndIsCorrect(
                userId = userId,
                isCorrect = isCorrect,
                size = size,
                offset = offset
            )
        }

        val countDeferred = async {
            submissionCoroutineRepository.countAllByUserIdAndIsCorrect(
                userId = userId,
                isCorrect = isCorrect
            )
        }

        val submissionPage = submissionsDeferred.await()
        val totalElements = countDeferred.await()

        val totalPages = (totalElements + size - 1) / size

        return@coroutineScope PageResponse(
            results = submissionPage.map { it.toModel() },
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size,
        )
    }

    override suspend fun findPageByUserIdAndIsCorrectAndQuestionIds(
        userId: Long,
        isCorrect: Boolean,
        questionIds: List<Long>,
        size: Int,
        page: Int
    ): PageResponse<Submission> = coroutineScope {
        val safePage = maxOf(page, 1)
        val offset = (safePage - 1) * size

        val submissionsDeferred = async {
            submissionCoroutineRepository.findPageByUserIdAndIsCorrectAndQuestionIds(
                userId = userId,
                isCorrect = isCorrect,
                questionIds = questionIds,
                size = size,
                offset = offset
            )
        }

        val countDeferred = async {
            submissionCoroutineRepository.countAllByUserIdAndIsCorrectAndQuestionIdIn(
                userId = userId,
                isCorrect = isCorrect,
                questionIds = questionIds
            )
        }

        val submissionPage = submissionsDeferred.await()
        val totalElements = countDeferred.await()

        val totalPages = (totalElements + size - 1) / size

        return@coroutineScope PageResponse(
            results = submissionPage.map { it.toModel() },
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size,
        )
    }
}