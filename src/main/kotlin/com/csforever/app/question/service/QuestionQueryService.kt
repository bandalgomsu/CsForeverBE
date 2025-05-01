package com.csforever.app.question.service

import com.csforever.app.question.dto.QuestionQueryResponse
import com.csforever.app.question.implement.QuestionFinder
import org.springframework.stereotype.Service

@Service
class QuestionQueryService(
    private val questionFinder: QuestionFinder,
) {

    suspend fun findRandomByTags(tags: List<String>): QuestionQueryResponse.QuestionInfo {
        val question = questionFinder.findRandomByTags(tags)

        return question.toInfo()
    }
}