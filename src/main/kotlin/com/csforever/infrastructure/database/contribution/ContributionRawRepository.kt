package com.csforever.infrastructure.database.contribution

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ContributionRawRepository(
    private val databaseClient: DatabaseClient
) {

    suspend fun analyzeByDateBetween(
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val sql = """
            INSERT INTO contribution (
                user_id,
                date,
                count
            )
            SELECT 
                user_id,
                DATE(created_at) as date,
                COUNT(*) as count
            FROM submission
            WHERE created_at BETWEEN :startDate AND :endDate
              AND user_id != 0
            GROUP BY user_id,date
            ON DUPLICATE KEY UPDATE 
                count = VALUES(count)
        """.trimIndent()

        databaseClient.sql(sql)
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .fetch()
            .rowsUpdated()
            .awaitSingle()
    }
}