package com.csforever.app.domain.question.implement

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.question.dao.QuestionDao
import com.csforever.app.domain.question.model.Question
import com.csforever.app.domain.question.model.QuestionTag
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class QuestionPaginationFinder(
    private val questionDao: QuestionDao,
) {
    private val log = LoggerFactory.getLogger(QuestionPaginationFinder::class.java)

    suspend fun findPageByTag(
        tag: QuestionTag,
        size: Int = 5,
        page: Int = 1
    ): PageResponse<Question> {
        return questionDao.findPageByTag(
            tag = tag,
            size = size,
            page = page
        )
    }
}