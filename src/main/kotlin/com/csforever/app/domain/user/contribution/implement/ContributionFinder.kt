package com.csforever.app.domain.user.contribution.implement

import com.csforever.app.domain.user.contribution.dao.ContributionDao
import com.csforever.app.domain.user.contribution.model.Contribution
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ContributionFinder(
    private val contributionDao: ContributionDao
) {

    suspend fun findAllByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Contribution> {
        return contributionDao.findAllByUserIdAndDateBetween(userId, startDate, endDate)
    }
}