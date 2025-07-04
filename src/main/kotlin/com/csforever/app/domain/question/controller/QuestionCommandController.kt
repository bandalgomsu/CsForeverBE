package com.csforever.app.domain.question.controller

import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.domain.question.dto.QuestionCommandRequest
import com.csforever.app.domain.question.dto.QuestionCommandResponse
import com.csforever.app.domain.question.service.QuestionCommandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/questions")
class QuestionCommandController(
    private val questionCommandService: QuestionCommandService
) {

    @PostMapping
    suspend fun submit(
        @RequestBody request: QuestionCommandRequest.QuestionSubmitRequest,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<QuestionCommandResponse.QuestionSubmitResponse> {
        request.validateAnswerLength()

        val response = questionCommandService.submitQuestion(request, authorizationContext.user)

        return ResponseEntity.ok(response)
    }
}