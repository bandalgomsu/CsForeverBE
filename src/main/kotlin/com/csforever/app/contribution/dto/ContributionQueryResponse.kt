package com.csforever.app.contribution.dto

import com.csforever.app.contribution.model.Contribution
import java.time.LocalDate

class ContributionQueryResponse {

    data class ContributionInfos(
        val contributions: List<ContributionInfo>,
    )

    data class ContributionInfo(
        val date: LocalDate,
        val count: Int,
        val level: Int
    ) {
        companion object {
            fun createByContribution(
                contribution: Contribution
            ): ContributionInfo {
                return ContributionInfo(
                    date = contribution.date,
                    count = contribution.count,
                    level = contribution.level
                )
            }
        }
    }
}