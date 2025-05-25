package com.csforever.app.admin.question

import com.csforever.app.question.implement.QuestionDeleter
import com.csforever.app.question.implement.QuestionInserter
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