package com.csforever.infrastructure.database.term

import com.csforever.app.term.model.Term
import com.csforever.infrastructure.database.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("term")
class TermEntity(
    val id: Long? = null,
    val term: String,
    val definition: String,
) : BaseEntity() {
    fun toModel(): Term {
        return Term(
            id = id,
            term = term,
            definition = definition,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}