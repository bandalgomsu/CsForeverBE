package com.csforever.infrastructure.database.term

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TermCoroutineRepository : CoroutineCrudRepository<TermEntity, Long> {
    suspend fun findByTerm(term: String): TermEntity?
}