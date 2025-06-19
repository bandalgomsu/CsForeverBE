package com.csforever.app.admin.contribution

import com.csforever.app.common.scope.CustomScope
import com.csforever.app.contribution.implement.ContributionAnalyzer
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ContributionAdminService(
    private val contributionAnalyzer: ContributionAnalyzer
) {

    suspend fun analyzeContribution(
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        CustomScope.fireAndForget.let {
            contributionAnalyzer.analyzeContribution(
                startDate = startDate,
                endDate = endDate
            )
        }
    }
}