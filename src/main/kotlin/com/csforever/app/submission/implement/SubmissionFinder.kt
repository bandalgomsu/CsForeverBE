package com.csforever.app.submission.implement

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.submission.dao.SubmissionDao
import com.csforever.app.submission.model.Submission
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
}