package com.csforever.app.domain.term.model

import com.csforever.app.domain.term.dto.TermResponse
import java.time.LocalDateTime

class Term(
    val id: Long? = null,
    val term: String,
    val definition: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun toInfo(): TermResponse.TermInfo {
        return TermResponse.TermInfo(
            term = term,
            definition = definition
        )
    }
}