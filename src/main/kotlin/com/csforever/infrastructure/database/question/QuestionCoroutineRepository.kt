package com.csforever.infrastructure.database.question

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface QuestionCoroutineRepository : CoroutineCrudRepository<QuestionEntity, Long> {
}