package com.csforever.app.domain.admin.question

import com.csforever.app.domain.question.implement.QuestionDeleter
import com.csforever.app.domain.question.implement.QuestionInserter
import org.springframework.stereotype.Service

@Service
class QuestionAdminService(
    private val questionInserter: QuestionInserter,
    private val questionDeleter: QuestionDeleter
) {

    suspend fun reloadByTagToRedisSet() {
        questionDeleter.deleteAllCacheByTagPatternToRedisSet()
        questionInserter.setAllCacheByTagToRedisSet()
    }
}