package com.csforever.app.term.controller

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.term.dto.TermResponse
import com.csforever.app.term.exception.TermErrorCode
import com.csforever.app.term.service.TermQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/terms")
class TermQueryController(
    private val termQueryService: TermQueryService
) {

    @GetMapping("/{term}")
    suspend fun findByTerm(@PathVariable term: String): ResponseEntity<TermResponse.TermInfo> {
        if (term.length > 30 || term.isBlank()) {
            throw BusinessException(TermErrorCode.INVALID_TERM_INPUT)
        }

        val response = termQueryService.findByTerm(term)

        return ResponseEntity.ok(response)
    }
}