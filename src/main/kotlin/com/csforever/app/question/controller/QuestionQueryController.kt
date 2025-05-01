package com.csforever.app.question.controller

import com.csforever.app.question.dto.QuestionQueryResponse
import com.csforever.app.question.service.QuestionQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/questions")
class QuestionQueryController(
    private val questionQueryService: QuestionQueryService,
) {

    @GetMapping
    suspend fun findRandomByTags(@RequestParam tags: List<String>): ResponseEntity<QuestionQueryResponse.QuestionInfo> {
        val response = questionQueryService.findRandomByTags(tags)

        return ResponseEntity.ok(response)
    }
}