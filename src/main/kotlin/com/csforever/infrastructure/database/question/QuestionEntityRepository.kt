package com.csforever.infrastructure.database.question

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.question.dao.QuestionDao
import com.csforever.app.domain.question.model.Question
import com.csforever.app.domain.question.model.QuestionTag
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    override suspend fun findPageByTag(
        tag: QuestionTag,
        size: Int,
        page: Int
    ): PageResponse<Question> = coroutineScope {
        val safePage = maxOf(page, 1)
        val offset = (safePage - 1) * size

        val questionsDeferred = async {
            questionCoroutineRepository.findPageByTag(
                tag = tag,
                size = size,
                offset = offset
            )
        }

        val countDeferred = async {
            questionCoroutineRepository.countAllByTag(
                tag = tag,
            )
        }

        val questionPage = questionsDeferred.await()
        val totalElements = countDeferred.await()

        val totalPages = (totalElements + size - 1) / size

        return@coroutineScope PageResponse(
            results = questionPage.map { it.toModel() },
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size,
        )
    }
}

