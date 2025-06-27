package com.csforever.infrastructure.database.submission

import com.csforever.app.domain.user.submission.model.Submission
import com.csforever.infrastructure.database.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("submission")
class SubmissionEntity(
    @Id
    val id: Long? = null,
    @Column("user_id")
    val userId: Long,
    @Column("question_id")
    val questionId: Long,
    @Column("answer")
    val answer: String,
    @Column("is_correct")
    val isCorrect: Boolean,
    @Column("feedback")
    val feedback: String,
) : BaseEntity() {

    fun toModel(): Submission {
        return Submission(
            id = id,
            userId = userId,
            questionId = questionId,
            answer = answer,
            isCorrect = isCorrect,
            feedback = feedback,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}