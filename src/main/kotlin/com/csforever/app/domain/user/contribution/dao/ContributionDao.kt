package com.csforever.app.domain.user.contribution.dao

import com.csforever.app.domain.user.contribution.model.Contribution
import java.time.LocalDate

interface ContributionDao {
    suspend fun findAllByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Contribution>

    suspend fun analyzeByDateBetween(
        startDate: LocalDate,
        endDate: LocalDate

    )
}