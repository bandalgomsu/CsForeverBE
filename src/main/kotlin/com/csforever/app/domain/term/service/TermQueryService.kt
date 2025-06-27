package com.csforever.app.domain.term.service

import com.csforever.app.domain.term.dto.TermResponse
import com.csforever.app.domain.term.implement.TermFinder
import org.springframework.stereotype.Service

@Service
class TermQueryService(
    private val termFinder: TermFinder
) {

    suspend fun findByTerm(term: String): TermResponse.TermInfo {
        val termModel = termFinder.findOrCreateByTerm(term)

        return termModel.toInfo()
    }
}