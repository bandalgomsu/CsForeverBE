package com.csforever.infrastructure.database.question

import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.model.Question
import org.springframework.stereotype.Repository

@Repository
class QuestionEntityRepository(
    private val questionCoroutineRepository: QuestionCoroutineRepository
) : QuestionDao {

    override suspend fun findById(questionId: Long): Question? {
        return questionCoroutineRepository.findById(questionId)?.toModel()
    }

}