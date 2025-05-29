package com.csforever.app.ranking.model

import java.time.LocalDateTime

class Ranking(
    val id: Long? = null,
    val ranking: Long,
    val type: RankingType,
    val correctSubmissionCount: Long,
    val userId: Long,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}