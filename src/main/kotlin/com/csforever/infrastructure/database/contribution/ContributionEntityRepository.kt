package com.csforever.infrastructure.database.contribution

import com.csforever.app.domain.user.contribution.dao.ContributionDao
import com.csforever.app.domain.user.contribution.model.Contribution
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ContributionEntityRepository(
    private val contributionCoroutineRepository: ContributionCoroutineRepository,
    private val contributionRawRepository: ContributionRawRepository
) : ContributionDao {

    override suspend fun findAllByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Contribution> {
        return contributionCoroutineRepository.findAllByUserIdAndDateBetween(userId, startDate, endDate)
            .map { it.toModel() }
    }

    override suspend fun analyzeByDateBetween(startDate: LocalDate, endDate: LocalDate) {
        contributionRawRepository.analyzeByDateBetween(
            startDate = startDate,
            endDate = endDate
        )
    }
}