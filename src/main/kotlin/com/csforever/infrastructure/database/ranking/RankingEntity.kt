package com.csforever.infrastructure.database.ranking

import com.csforever.app.domain.ranking.model.Ranking
import com.csforever.app.domain.ranking.model.RankingType
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_ranking")
class RankingEntity(
    @Column("id")
    val id: Long? = null,
    @Column("ranking")
    val ranking: Long,
    @Column("type")
    val type: RankingType,
    @Column("correct_submission_count")
    val correctSubmissionCount: Long,
    @Column("user_id")
    val userId: Long,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
) {
    fun toModel(): Ranking {
        return Ranking(
            id = id,
            ranking = ranking,
            type = type,
            correctSubmissionCount = correctSubmissionCount,
            userId = userId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}