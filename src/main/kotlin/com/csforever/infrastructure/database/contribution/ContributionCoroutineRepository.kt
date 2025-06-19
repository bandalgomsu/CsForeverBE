package com.csforever.infrastructure.database.contribution

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDate

interface ContributionCoroutineRepository : CoroutineCrudRepository<ContributionEntity, Long> {
    suspend fun findAllByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<ContributionEntity>

}