package com.csforever.infrastructure.database.ranking

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component

@Component
class RankingRawRepository(
    private val databaseClient: DatabaseClient
) {

    suspend fun analyzeRanking() {
        val sql = """
            INSERT INTO user_ranking (
                user_id,
                ranking,
                type,
                correct_submission_count,
                updated_at
            )
            SELECT 
                user_id,
                RANK() OVER (ORDER BY COUNT(DISTINCT question_id) DESC),
                'TOTAL',
                COUNT(DISTINCT question_id),
                CONVERT_TZ(NOW(), 'UTC', 'Asia/Seoul')
            FROM submission
            WHERE is_correct = 1
              AND user_id != 0
            GROUP BY user_id
            ON DUPLICATE KEY UPDATE 
                ranking = VALUES(ranking),
                correct_submission_count = VALUES(correct_submission_count),
                updated_at = VALUES(updated_at);
        """.trimIndent()

        databaseClient.sql(sql)
            .fetch()
            .rowsUpdated()
            .awaitSingle()
    }
}