package com.csforever.app.domain.user.contribution.implement

import com.csforever.app.domain.user.contribution.dao.ContributionDao
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ContributionAnalyzer(
    private val contributionDao: ContributionDao
) {

    suspend fun analyzeContribution(
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        contributionDao.analyzeByDateBetween(
            startDate = startDate,
            endDate = endDate
        )
    }
}