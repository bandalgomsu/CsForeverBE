package com.csforever.app.question.controller

import com.csforever.app.question.dto.QuestionCommandRequest
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.service.QuestionCommandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/question")
class QuestionCommandController(
    private val questionCommandService: QuestionCommandService
) {

    @PostMapping
    suspend fun submit(@RequestBody request: QuestionCommandRequest.QuestionSubmitRequest): ResponseEntity<QuestionCommandResponse.QuestionSubmitResponse> {
        val response = questionCommandService.submitQuestion(request, 1)

        return ResponseEntity.ok(response)
    }
}