package com.csforever.infrastructure.database.submission

import com.csforever.app.submission.dao.SubmissionDao
import com.csforever.app.submission.model.Submission
import org.springframework.stereotype.Repository

@Repository
class SubmissionEntityRepository(
    private val submissionCoroutineRepository: SubmissionCoroutineRepository
) : SubmissionDao {


    override suspend fun insert(submission: Submission): Submission {
        return submissionCoroutineRepository.save(
            SubmissionEntity(
                userId = submission.userId,
                questionId = submission.questionId,
                answer = submission.answer,
                isCorrect = submission.isCorrect,
                feedback = submission.feedback,
            )
        ).toModel()
    }
}