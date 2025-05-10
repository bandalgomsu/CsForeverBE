package com.csforever.infrastructure.database.submission

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface SubmissionCoroutineRepository : CoroutineCrudRepository<SubmissionEntity, Long> {
}