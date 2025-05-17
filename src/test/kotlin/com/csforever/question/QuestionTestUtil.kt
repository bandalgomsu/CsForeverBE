package com.csforever.question

import com.csforever.app.question.dto.QuestionCommandRequest
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.model.LLMQuestionSubmitCommand
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import com.csforever.infrastructure.database.question.QuestionEntity

class QuestionTestUtil {
    companion object {
        const val QUESTION_ID1 = 1L
        const val QUESTION_ID2 = 2L

        const val QUESTION = "test_question"
        const val BEST_ANSWER = "test_best_answer"

        val TAG1 = QuestionTag.C_SHARP
        val TAG2 = QuestionTag.JAVA

        const val ANSWER = "test_answer"
        const val USER_NICKNAME = "test_nickname"
        const val TEST_FEEDBACK = "test_feedback"

        fun getQuestionEntity1() = QuestionEntity(
            question = QUESTION,
            bestAnswer = BEST_ANSWER,
            tag = TAG1
        )

        fun getQuestionEntity2() = QuestionEntity(
            question = QUESTION,
            bestAnswer = BEST_ANSWER,
            tag = TAG2
        )

        fun getQuestion1() = Question(
            id = QUESTION_ID1,
            question = QUESTION,
            bestAnswer = BEST_ANSWER,
            tag = TAG1
        )

        fun getQuestion2() = Question(
            id = QUESTION_ID2,
            question = QUESTION,
            bestAnswer = BEST_ANSWER,
            tag = TAG2
        )

        fun getLLMQuestionSubmitCommand() = LLMQuestionSubmitCommand(
            question = QUESTION,
            answer = ANSWER,
            bestAnswer = BEST_ANSWER,
        )

        fun getQuestionSubmitResponse() = QuestionCommandResponse.QuestionSubmitResponse(
            isCorrect = true,
            feedback = TEST_FEEDBACK
        )

        fun getQuestionSubmitRequest() = QuestionCommandRequest.QuestionSubmitRequest(
            questionId = QUESTION_ID1,
            answer = ANSWER
        )
    }
}