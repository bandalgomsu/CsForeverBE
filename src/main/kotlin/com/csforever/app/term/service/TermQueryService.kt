package com.csforever.app.term.service

import com.csforever.app.term.dto.TermResponse
import com.csforever.app.term.implement.TermFinder
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