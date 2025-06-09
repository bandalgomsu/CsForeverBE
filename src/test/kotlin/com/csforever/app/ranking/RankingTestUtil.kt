package com.csforever.app.ranking

import com.csforever.app.ranking.model.Ranking
import com.csforever.app.ranking.model.RankingType

class RankingTestUtil {
    companion object {
        fun createTestRanking(rankingId: Long? = null, userId: Long, correctSubmissionCount: Long): Ranking {
            return Ranking(
                id = 1L,
                userId = 1L,
                ranking = 1,
                type = RankingType.TOTAL,
                correctSubmissionCount = 10,
            )
        }
    }
}