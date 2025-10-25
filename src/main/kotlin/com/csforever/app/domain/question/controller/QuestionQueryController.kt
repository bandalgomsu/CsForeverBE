package com.csforever.app.domain.question.controller

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.question.dto.QuestionQueryResponse
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.app.domain.question.service.QuestionQueryService
import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/questions")
class QuestionQueryController(
    private val questionQueryService: QuestionQueryService,
) {

    @GetMapping("/random")
    suspend fun findRandomQuestionByTags(
        @RequestParam tags: List<QuestionTag>,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<QuestionQueryResponse.QuestionInfo> {
        val response = questionQueryService.findRandomByTags(tags, authorizationContext.user!!.id!!)

        return ResponseEntity.ok(response)
    }

    @GetMapping()
    suspend fun findPageQuestion(
        @RequestParam tag: QuestionTag,

        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<PageResponse<QuestionQueryResponse.QuestionInfo>> {
        val response = questionQueryService.findPage(tag, page, size, authorizationContext.user!!.id!!)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{questionId}")
    suspend fun findQuestionById(
        @PathVariable questionId: Long,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<QuestionQueryResponse.QuestionInfo> {
        val response = questionQueryService.findById(questionId, authorizationContext.user!!.id!!)
        
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