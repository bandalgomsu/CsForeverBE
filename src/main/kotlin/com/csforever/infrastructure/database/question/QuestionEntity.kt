package com.csforever.infrastructure.database.question

import com.csforever.app.domain.question.model.Question
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.infrastructure.database.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("question")
class QuestionEntity(
    @Id
    val id: Long? = null,
    @Column("question")
    val question: String,
    @Column("best_answer")
    val bestAnswer: String,
    @Column("tag")
    val tag: QuestionTag,
) : BaseEntity() {

    fun toModel(): Question {
        return Question(
            id = id,
            question = question,
            bestAnswer = bestAnswer,
            tag = tag,
        )
    }
}