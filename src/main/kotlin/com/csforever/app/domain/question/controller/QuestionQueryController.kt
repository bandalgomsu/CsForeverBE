package com.csforever.app.domain.question.controller

import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.domain.question.dto.QuestionQueryResponse
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.app.domain.question.service.QuestionQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/questions")
class QuestionQueryController(
    private val questionQueryService: QuestionQueryService,
) {

    @GetMapping
    suspend fun findRandomByTags(
        @RequestParam tags: List<QuestionTag>,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<QuestionQueryResponse.QuestionInfo> {
        val response = questionQueryService.findRandomByTags(tags, authorizationContext.user!!.id!!)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/bestAnswer/{questionId}")
    suspend fun findBestAnswerById(
        @PathVariable questionId: Long,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<QuestionQueryResponse.QuestionInfo> {
        val response = questionQueryService.findBestAnswerById(questionId, authorizationContext.user)

        return ResponseEntity.ok(response)
    }
}