package com.csforever.app.question

import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag

class QuestionTestUtil {
    companion object {

        fun createTestQuestion(questionId: Long? = null): Question {
            return Question(
                id = questionId,
                question = "Sample Question",
                bestAnswer = "Sample Answer",
                tag = QuestionTag.SPRING
            )
        }
    }
}