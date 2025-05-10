package com.csforever.app.question.service

import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.scope.CustomScope
import com.csforever.app.question.dto.QuestionCommandRequest
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.model.LLMQuestionSubmitCommand
import com.csforever.app.submission.implement.SubmissionInserter
import com.csforever.app.user.model.User
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class QuestionCommandService(
    private var llmClient: LLMClient,
    private val submissionInserter: SubmissionInserter,

    private val questionFinder: QuestionFinder,
) {
    suspend fun submitQuestion(
        request: QuestionCommandRequest.QuestionSubmitRequest,
        user: User?
    ): QuestionCommandResponse.QuestionSubmitResponse {
        val question = questionFinder.findById(request.questionId)

        val command = LLMQuestionSubmitCommand(
            question.question,
            request.answer,
            question.bestAnswer,
            user?.nickname ?: "훌륭한 개발자"
        )

        val response = llmClient.requestByCommand(command)

        user?.let {
            CustomScope.fireAndForget.launch {
                submissionInserter.insert(
                    userId = user.id!!,
                    questionId = question.id!!,
                    answer = request.answer,
                    isCorrect = response.isCorrect,
                    feedback = response.feedback
                )
            }
        }

        return response
    }
}