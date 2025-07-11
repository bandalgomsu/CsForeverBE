package com.csforever.app.domain.question.service

import com.csforever.app.common.scope.CustomScope
import com.csforever.app.domain.question.dto.QuestionCommandRequest
import com.csforever.app.domain.question.dto.QuestionCommandResponse
import com.csforever.app.domain.question.implement.QuestionSubmitter
import com.csforever.app.domain.user.profile.model.User
import com.csforever.app.domain.user.submission.implement.SubmissionInserter
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class QuestionCommandService(
    private val submissionInserter: SubmissionInserter,
    private val questionSubmitter: QuestionSubmitter,
) {
    suspend fun submitQuestion(
        request: QuestionCommandRequest.QuestionSubmitRequest,
        user: User?
    ): QuestionCommandResponse.QuestionSubmitResponse {
        val response = questionSubmitter.submit(request.questionId, request.answer, user)

        CustomScope.fireAndForget.launch {
            submissionInserter.insert(
                userId = user?.id ?: 0, // 게스트의 user_id = 0 이라 하자
                questionId = request.questionId,
                answer = request.answer,
                isCorrect = response.isCorrect,
                feedback = response.feedback
            )
        }

        return response
    }
}