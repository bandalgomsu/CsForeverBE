package com.csforever.app.domain.admin.ranking

import com.csforever.app.common.scope.CustomScope
import com.csforever.app.domain.ranking.implement.RankingAnalyzer
import org.springframework.stereotype.Service

@Service
class RankingAdminService(
    private val rankingAnalyzer: RankingAnalyzer
) {

    suspend fun analyzeRanking() {
        CustomScope.fireAndForget.let {
            rankingAnalyzer.analyzeRanking()
        }
    }
}