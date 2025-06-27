package com.csforever.app.domain.user.contribution.service

import com.csforever.app.domain.user.contribution.dto.ContributionQueryResponse
import com.csforever.app.domain.user.contribution.implement.ContributionFinder
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ContributionQueryService(
    private val contributionFinder: ContributionFinder
) {

    suspend fun findAllByUserIdAndYearUntilToday(
        userId: Long,
        year: Int
    ): ContributionQueryResponse.ContributionInfos {
        val contributionInfos = contributionFinder.findAllByUserIdAndDateBetween(
            userId = userId,
            startDate = year.let { LocalDate.of(it, 1, 1) },
            endDate = LocalDate.now()
        ).map {
            ContributionQueryResponse.ContributionInfo.createByContribution(it)
        }

        return ContributionQueryResponse.ContributionInfos(contributionInfos)
    }
}