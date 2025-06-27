package com.csforever.app.domain.user.submission.implement

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.user.submission.dao.SubmissionDao
import com.csforever.app.domain.user.submission.model.Submission
import org.springframework.stereotype.Component

@Component
class SubmissionFinder(
    private val submissionDao: SubmissionDao
) {

    suspend fun findPageByUserIdAndIsCorrect(
        userId: Long,
        isCorrect: Boolean = true,
        size: Int = 5,
        page: Int = 1
    ): PageResponse<Submission> {
        return submissionDao.findPageByUserIdAndIsCorrect(
            userId = userId,
            isCorrect = isCorrect,
            size = size,
            page = page
        )
    }

    suspend fun findPageByUserIdAndIsCorrectAndQuestionIds(
        userId: Long,
        isCorrect: Boolean = true,
        questionIds: List<Long>,
        size: Int = 5,
        page: Int = 1
    ): PageResponse<Submission> {
        return submissionDao.findPageByUserIdAndIsCorrectAndQuestionIds(
            userId = userId,
            isCorrect = isCorrect,
            questionIds = questionIds,
            size = size,
            page = page
        )
    }
}