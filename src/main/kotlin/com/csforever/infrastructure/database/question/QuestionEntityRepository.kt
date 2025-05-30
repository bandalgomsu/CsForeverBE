package com.csforever.infrastructure.database.question

import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository

@Repository
class QuestionEntityRepository(
    private val questionCoroutineRepository: QuestionCoroutineRepository
) : QuestionDao {
    override suspend fun findById(questionId: Long): Question? {
        return questionCoroutineRepository.findById(questionId)?.toModel()
    }

    override suspend fun findIdsByTag(tag: QuestionTag): List<Long> {
        return questionCoroutineRepository.findIdsByTag(tag)
            .toList()
    }

    override suspend fun findAll(): List<Question> {
        return questionCoroutineRepository.findAll()
            .toList()
            .map { it.toModel() }
    }

    override suspend fun findAllByIdIn(questionIds: List<Long>): List<Question> {
        return questionCoroutineRepository.findAllByIdIn(questionIds)
            .toList()
            .map { it.toModel() }
    }

    override suspend fun findAllByTagAndIdIn(tag: QuestionTag, questionIds: List<Long>): List<Question> {
        return questionCoroutineRepository.findAllByTagAndIdIn(tag, questionIds)
            .toList()
            .map { it.toModel() }
    }
}

