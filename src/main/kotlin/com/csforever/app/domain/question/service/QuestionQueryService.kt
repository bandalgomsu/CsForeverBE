package com.csforever.app.domain.question.service

import com.csforever.app.common.scope.CustomScope
import com.csforever.app.domain.question.dto.QuestionQueryResponse
import com.csforever.app.domain.question.implement.QuestionFinder
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.app.domain.user.profile.model.User
import com.csforever.app.domain.user.submission.implement.SubmissionExistChecker
import com.csforever.app.domain.user.submission.implement.SubmissionInserter
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class QuestionQueryService(
    private val questionFinder: QuestionFinder,
    private val submissionInserter: SubmissionInserter,
    private val submissionExistChecker: SubmissionExistChecker
) {

    suspend fun findRandomByTags(tags: List<QuestionTag>, userId: Long): QuestionQueryResponse.QuestionInfo {
        val question = questionFinder.findRandomByTags(tags)
        val isSolution = submissionExistChecker.existByUserIdAndQuestionId(
            userId = userId,
            questionId = question.id!!,
            isCorrect = true
        )

        return question.toInfo(isSolution)
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