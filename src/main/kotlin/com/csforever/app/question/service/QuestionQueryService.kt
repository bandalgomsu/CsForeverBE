package com.csforever.app.question.service

import com.csforever.app.common.scope.CustomScope
import com.csforever.app.question.dto.QuestionQueryResponse
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.model.QuestionTag
import com.csforever.app.submission.implement.SubmissionInserter
import com.csforever.app.user.model.User
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class QuestionQueryService(
    private val questionFinder: QuestionFinder,
    private val submissionInserter: SubmissionInserter,
) {

    suspend fun findRandomByTags(tags: List<QuestionTag>): QuestionQueryResponse.QuestionInfo {
        val question = questionFinder.findRandomByTags(tags)

        return question.toInfo()
    }

    suspend fun findBestAnswerById(questionId: Long, user: User?): QuestionQueryResponse.QuestionInfo {
        val question = questionFinder.findById(questionId)

        CustomScope.fireAndForget.launch {
            submissionInserter.insert(
                userId = user?.id ?: 0, // 게스트의 user_id = 0 이라 하자
                questionId = questionId,
                answer = "", // 답안 보기이므로 빈 문자열
                isCorrect = false, // 답안 보기이므로 정답 여부는 false
                feedback = "" // 답안 보기이므로 빈 문자열
            )
        }

        return question.toInfo()
    }
}