package com.csforever.app.domain.ranking.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.domain.ranking.dao.RankingDao
import com.csforever.app.domain.ranking.exception.RankingErrorCode
import com.csforever.app.domain.ranking.model.Ranking
import org.springframework.stereotype.Component

@Component
class RankingFinder(
    private val rankingDao: RankingDao
) {

    suspend fun findByUserId(userId: Long): Ranking {
        return rankingDao.findByUserId(userId) ?: throw BusinessException(RankingErrorCode.RANKING_NOT_FOUND)
    }
}