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

        val contributionDates = contributionInfos
            .map { it.date }
            .toSet()
            .sortedDescending()

        var continuosDay = 0
        var currentDate = LocalDate.now().minusDays(1);

        // 오늘부터 역순으로 연속된 날짜 확인
        while (contributionDates.contains(currentDate)) {
            continuosDay++
            currentDate = currentDate.minusDays(1)
        }

        return ContributionQueryResponse.ContributionInfos(contributionInfos, continuosDay)
    }
}