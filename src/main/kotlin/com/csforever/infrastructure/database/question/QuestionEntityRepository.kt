package com.csforever.infrastructure.database.question

import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository
import kotlin.random.Random

@Repository
class QuestionEntityRepository(
    private val questionCoroutineRepository: QuestionCoroutineRepository
) : QuestionDao {
    override suspend fun findById(questionId: Long): Question? {
        return questionCoroutineRepository.findById(questionId)?.toModel()
    }

    override suspend fun findRandomByTag(tags: List<QuestionTag>): Question? {
        val count = questionCoroutineRepository.countByTagIn(tags)
        val randomOffset = Random.nextLong(0, count)

        return questionCoroutineRepository.findPageByTagIn(1, randomOffset.toInt(), tags)
            .toList()
            .first()
            .toModel()
    }

    override suspend fun findAllByIdIn(questionIds: List<Long>): List<Question> {
        return questionCoroutineRepository.findAllByIdIn(questionIds)
            .toList()
            .map { it.toModel() }
    }
}

