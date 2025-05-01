package com.csforever.app.question.service

import com.csforever.app.common.llm.LLMClient
import com.csforever.app.question.dto.QuestionCommandRequest
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.model.LLMQuestionSubmitCommand
import org.springframework.stereotype.Service

@Service
class QuestionCommandService(
    private var llmClient: LLMClient,

    private val questionFinder: QuestionFinder,
) {

    //todo : Auth 추가되면 User 관련 로직 추가 (비동기)
    suspend fun submitQuestion(
        request: QuestionCommandRequest.QuestionSubmitRequest,
        userId: Long
    ): QuestionCommandResponse.QuestionSubmitResponse {
        val question = questionFinder.findById(request.questionId)

        val command = LLMQuestionSubmitCommand(question.question, request.answer, question.bestAnswer)

        return llmClient.requestByCommand(command)
    }
}