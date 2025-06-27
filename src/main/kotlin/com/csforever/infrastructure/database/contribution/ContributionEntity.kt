package com.csforever.infrastructure.database.contribution

import com.csforever.app.domain.user.contribution.model.Contribution
import com.csforever.infrastructure.database.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("contribution")
class ContributionEntity(
    @Id
    val id: Long? = null,
    val date: LocalDate,
    val count: Int,
    val userId: Long,
) : BaseEntity() {
    fun toModel(): Contribution {
        return Contribution(
            id = id,
            date = date,
            count = count,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}