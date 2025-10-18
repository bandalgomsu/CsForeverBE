package com.csforever.infrastructure.database.donation

import com.csforever.app.domain.donation.model.Donation
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.infrastructure.database.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("donation")
class DonationEntity(
    @Id
    var id: Long? = null,
    @Column("question")
    val question: String,
    @Column("best_answer")
    val bestAnswer: String,
    @Column("tag")
    val tag: QuestionTag,
    @Column
    val userId: Long,
    @Column
    val questionId: Long? = null,
    override var createdAt: LocalDateTime? = null,
    override var updatedAt: LocalDateTime? = null,
) : BaseEntity(createdAt, updatedAt) {

    fun toModel(): Donation {
        return Donation(
            id = id,
            question = question,
            bestAnswer = bestAnswer,
            tag = tag,
            userId = userId,
            questionId = questionId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}